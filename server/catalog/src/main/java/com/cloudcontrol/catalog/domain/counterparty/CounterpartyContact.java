package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.rule.CounterpartyContactRules;
import com.cloudcontrol.catalog.domain.counterparty.vo.PhoneNumber;
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
public class CounterpartyContact implements Entity<Counterparty, CounterpartyContact.CounterpartyContactId>, Auditable {

    public record CounterpartyContactId(Long value) implements Identifier {
    }

    private final CounterpartyContactId id;
    @Nullable
    private String personName;
    @NonNull
    private PhoneNumber phone;

    @Getter(AccessLevel.NONE)
    @FieldNameConstants.Exclude
    private final List<AuditEntry> auditEntries;

    static @NonNull CounterpartyContact withPerson(
            @NonNull String personName,
            @NonNull PhoneNumber phone,
            @NonNull UserIdentifier changedBy
    ) {
        validatePersonName(personName);

        CounterpartyContact counterpartyContact = new CounterpartyContact(null, personName, phone, new ArrayList<>());

        AuditBuilder.forCreate(entity(), counterpartyContact.entityId(), changedBy)
                .field(Fields.personName, personName)
                .field(Fields.phone, phone.value())
                .buildInto(counterpartyContact);

        return counterpartyContact;
    }

    static @NonNull CounterpartyContact phoneOnly(@NonNull PhoneNumber phone, @NonNull UserIdentifier changedBy) {
        CounterpartyContact counterpartyContact = new CounterpartyContact(null, null, phone, new ArrayList<>());

        AuditBuilder.forCreate(entity(), counterpartyContact.entityId(), changedBy)
                .field(Fields.phone, phone.value())
                .buildInto(counterpartyContact);

        return counterpartyContact;
    }

    void updatePersonName(@Nullable String personName, @NonNull UserIdentifier changedBy) {
        validatePersonName(personName);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.personName, this.personName, personName)
                .buildInto(this);

        this.personName = personName;
    }

    void updatePhone(@NonNull PhoneNumber phone, @NonNull UserIdentifier changedBy) {
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.phone, this.phone.value(), phone.value())
                .buildInto(this);

        this.phone = phone;
    }

    public boolean hasContactPerson() {
        return this.personName != null && !this.personName.isBlank();
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

    private static void validatePersonName(@Nullable String value) {
        if (value != null && value.length() > CounterpartyContactRules.PersonName.MAX_LENGTH)
            throw new InvalidValueException(CounterpartyContactRules.PersonName.TOO_LONG, CounterpartyContactRules.PersonName.MAX_LENGTH);
    }

    private static @NonNull String entity() {
        return CounterpartyContact.class.getSimpleName();
    }

    private @NonNull String entityId() {
        return this.id != null ? this.id.value().toString() : "new";
    }
}
