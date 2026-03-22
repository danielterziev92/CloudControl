package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.rule.CounterpartyBankAccountRules;
import com.cloudcontrol.catalog.domain.counterparty.vo.Bic;
import com.cloudcontrol.catalog.domain.counterparty.vo.Iban;
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
public class CounterpartyBankAccount implements Entity<Counterparty, CounterpartyBankAccount.CounterpartyBankAccountId>, Auditable {

    public record CounterpartyBankAccountId(Long value) implements Identifier {
    }

    private final CounterpartyBankAccountId id;
    @NonNull
    private String name;
    @Nullable
    private String nameEn;
    @NonNull
    private Bic bic;
    @NonNull
    private Iban iban;
    private boolean isPrimary;

    @Getter(AccessLevel.NONE)
    @FieldNameConstants.Exclude
    private final List<AuditEntry> auditEntries;

    static @NonNull CounterpartyBankAccount create(
            @NonNull String name,
            @Nullable String nameEn,
            @NonNull Bic bic,
            @NonNull Iban iban,
            boolean isPrimary,
            @NonNull UserIdentifier changedBy
    ) {
        validateName(name);
        validateNameEn(nameEn);

        CounterpartyBankAccount counterpartyBankAccount = new CounterpartyBankAccount(
                null, name, nameEn, bic, iban, isPrimary, new ArrayList<>());

        AuditBuilder.forCreate(entity(), counterpartyBankAccount.entityId(), changedBy)
                .field(Fields.name, name)
                .field(Fields.nameEn, nameEn)
                .field(Fields.bic, bic.value())
                .field(Fields.iban, iban.value())
                .field(Fields.isPrimary, isPrimary ? "true" : "false")
                .buildInto(counterpartyBankAccount);

        return counterpartyBankAccount;
    }

    void updateName(@NonNull String name, @NonNull UserIdentifier changedBy) {
        validateName(name);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.name, this.name, name)
                .buildInto(this);

        this.name = name;
    }

    void updateNameEn(@NonNull String nameEn, @NonNull UserIdentifier changedBy) {
        validateNameEn(nameEn);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.nameEn, this.nameEn, nameEn)
                .buildInto(this);

        this.nameEn = nameEn;
    }

    void removeNameEn(@NonNull UserIdentifier changedBy) {
        AuditBuilder.forDelete(entity(), entityId(), changedBy)
                .field(Fields.nameEn, this.nameEn)
                .buildInto(this);

        this.nameEn = null;
    }

    void updateBic(@NonNull Bic bic, @NonNull UserIdentifier changedBy) {
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.bic, this.bic.value(), bic.value())
                .buildInto(this);

        this.bic = bic;
    }

    void updateIban(@NonNull Iban iban, @NonNull UserIdentifier changedBy) {
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.iban, this.iban.value(), iban.value())
                .buildInto(this);

        this.iban = iban;
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

    private static void validateName(@NonNull String value) {
        if (value.isBlank())
            throw new InvalidValueException(CounterpartyBankAccountRules.Name.BLANK);

        if (value.length() > CounterpartyBankAccountRules.Name.MAX_LENGTH)
            throw new InvalidValueException(CounterpartyBankAccountRules.Name.TOO_LONG, CounterpartyBankAccountRules.Name.MAX_LENGTH);
    }

    private static void validateNameEn(@Nullable String value) {
        if (value != null && value.length() > CounterpartyBankAccountRules.NameEn.MAX_LENGTH)
            throw new InvalidValueException(CounterpartyBankAccountRules.NameEn.TOO_LONG, CounterpartyBankAccountRules.NameEn.MAX_LENGTH);
    }

    private static @NonNull String entity() {
        return CounterpartyBankAccount.class.getSimpleName();
    }

    private @NonNull String entityId() {
        return this.id != null ? this.id.value().toString() : "new";
    }
}
