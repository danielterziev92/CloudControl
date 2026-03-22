package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.rule.CounterpartyEmailRules;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@FieldNameConstants
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CounterpartyEmailAddress implements Entity<Counterparty, CounterpartyEmailAddress.CounterpartyEmailAddressId>, Auditable {

    public record CounterpartyEmailAddressId(Long value) implements Identifier {
    }

    private final CounterpartyEmailAddressId id;
    @NonNull
    private String email;
    private boolean isPrimary;

    @Getter(AccessLevel.NONE)
    @FieldNameConstants.Exclude
    private final List<AuditEntry> auditEntries;

    static @NonNull CounterpartyEmailAddress create(
            @NonNull String email,
            boolean isPrimary,
            @NonNull UserIdentifier changedBy
    ) {
        validateEmail(email);

        CounterpartyEmailAddress counterpartyEmailAddress = new CounterpartyEmailAddress(null, email, isPrimary, new ArrayList<>());

        AuditBuilder.forCreate(entity(), counterpartyEmailAddress.entityId(), changedBy)
                .field(Fields.email, email)
                .field(Fields.isPrimary, isPrimary ? "true" : "false")
                .buildInto(counterpartyEmailAddress);

        return counterpartyEmailAddress;
    }

    void updateEmail(@NonNull String email, @NonNull UserIdentifier changedBy) {
        validateEmail(email);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.email, this.email, email)
                .buildInto(this);

        this.email = email;
    }

    void markAsPrimary(@NonNull UserIdentifier changedBy) {
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.isPrimary, this.isPrimary ? "true" : "false", "true")
                .buildInto(this);

        this.isPrimary = true;
    }

    void unmarkAsPrimary(@NonNull UserIdentifier changedBy) {
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.isPrimary, this.isPrimary ? "true" : "false", "false")
                .buildInto(this);

        this.isPrimary = false;
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

    private static void validateEmail(@NonNull String value) {
        if (value.isBlank())
            throw new InvalidValueException(CounterpartyEmailRules.Email.BLANK);

        if (value.length() > CounterpartyEmailRules.Email.MAX_LENGTH)
            throw new InvalidValueException(CounterpartyEmailRules.Email.TOO_LONG, CounterpartyEmailRules.Email.MAX_LENGTH);

        if (!CounterpartyEmailRules.Email.PATTERN.matcher(value).matches())
            throw new InvalidValueException(CounterpartyEmailRules.Email.INVALID, value);
    }

    private static @NonNull String entity() {
        return CounterpartyEmailAddress.class.getSimpleName();
    }

    private @NonNull String entityId() {
        return this.id != null ? this.id.value().toString() : "new";
    }
}
