package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.rule.PersonInfoRules;
import com.cloudcontrol.catalog.domain.counterparty.vo.PersonalIdentificationNumber;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import com.cloudcontrol.catalog.domain.shared.audit.AuditBuilder;
import com.cloudcontrol.catalog.domain.shared.audit.AuditEntry;
import com.cloudcontrol.catalog.domain.shared.audit.Auditable;
import com.cloudcontrol.catalog.domain.shared.audit.UserIdentifier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@FieldNameConstants
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonInfo implements Entity<Counterparty, PersonInfo.PersonInfoId>, Auditable {
    public record PersonInfoId(Long value) implements Identifier {
    }

    private final PersonInfoId id;
    @NonNull
    private String firstName;
    @Nullable
    private String middleName;
    @NonNull
    private String lastName;
    @NonNull
    private PersonalIdentificationNumber pin;
    @NonNull
    private CounterpartyRelationType type;

    @Getter(AccessLevel.NONE)
    @FieldNameConstants.Exclude
    private final List<AuditEntry> auditEntries;

    static @NonNull PersonInfo create(
            @NonNull String firstName,
            @Nullable String middleName,
            @NonNull String lastName,
            @NonNull PersonalIdentificationNumber pin,
            @NonNull CounterpartyRelationType type,
            @NonNull UserIdentifier changedBy
    ) {
        validateFirstName(firstName);
        validateMiddleName(middleName);
        validateLastName(lastName);

        PersonInfo personInfo = new PersonInfo(null, firstName, middleName, lastName, pin, type, new ArrayList<>());

        AuditBuilder.forCreate(entity(), "new", changedBy)
                .field(Fields.firstName, firstName)
                .field(Fields.middleName, middleName)
                .field(Fields.lastName, lastName)
                .field(Fields.pin, pin.value())
                .field(Fields.type, type.name())
                .buildInto(personInfo);

        return personInfo;
    }

    void updateFirstName(@NonNull String firstName, @NonNull UserIdentifier changedBy) {
        validateFirstName(firstName);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.firstName, this.firstName, firstName)
                .buildInto(this);

        this.firstName = firstName;
    }

    void updateMiddleName(@NonNull String middleName, @NonNull UserIdentifier changedBy) {
        validateMiddleName(middleName);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.middleName, this.middleName, middleName)
                .buildInto(this);

        this.middleName = middleName;
    }

    void removeMiddleName(@NonNull UserIdentifier changedBy) {
        AuditBuilder.forDelete(entity(), entityId(), changedBy)
                .field(Fields.middleName, this.middleName)
                .buildInto(this);

        this.middleName = null;
    }

    void updateLastName(@NonNull String lastName, @NonNull UserIdentifier changedBy) {
        validateLastName(lastName);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.lastName, this.lastName, lastName)
                .buildInto(this);

        this.lastName = lastName;
    }

    void updatePin(@NonNull PersonalIdentificationNumber pin, @NonNull UserIdentifier changedBy) {
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.pin, this.pin.value(), pin.value())
                .buildInto(this);

        this.pin = pin;
    }

    void updateType(@NonNull CounterpartyRelationType type, @NonNull UserIdentifier changedBy) {
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.type, this.type.name(), type.name())
                .buildInto(this);

        this.type = type;
    }

    public @NonNull String fullName() {
        if (middleName != null && !middleName.isBlank())
            return String.format("%s %s %s", firstName, middleName, lastName);
        return String.format("%s %s", firstName, lastName);
    }

    @Override
    public @NonNull List<AuditEntry> getAuditEntries() {
        return Collections.unmodifiableList(this.auditEntries);
    }

    @Override
    public void addAuditEntry(@NonNull AuditEntry entry) {
        this.auditEntries.add(entry);
    }

    @Override
    public void clearAuditEntries() {
        this.auditEntries.clear();
    }

    private static void validateFirstName(@NonNull String value) {
        if (value.isBlank())
            throw new InvalidValueException(PersonInfoRules.FirstName.BLANK);

        if (value.length() > PersonInfoRules.FirstName.MAX_LENGTH)
            throw new InvalidValueException(PersonInfoRules.FirstName.TOO_LONG, PersonInfoRules.FirstName.MAX_LENGTH);
    }

    private static void validateMiddleName(@Nullable String value) {
        if (value != null && value.length() > PersonInfoRules.MiddleName.MAX_LENGTH)
            throw new InvalidValueException(PersonInfoRules.MiddleName.TOO_LONG, PersonInfoRules.MiddleName.MAX_LENGTH);
    }

    private static void validateLastName(@NonNull String value) {
        if (value.isBlank())
            throw new InvalidValueException(PersonInfoRules.LastName.BLANK);

        if (value.length() > PersonInfoRules.LastName.MAX_LENGTH)
            throw new InvalidValueException(PersonInfoRules.LastName.TOO_LONG, PersonInfoRules.LastName.MAX_LENGTH);
    }

    private static @NonNull String entity() {
        return PersonInfo.class.getSimpleName();
    }

    private @NonNull String entityId() {
        return this.id != null ? this.id.value().toString() : "new";
    }
}
