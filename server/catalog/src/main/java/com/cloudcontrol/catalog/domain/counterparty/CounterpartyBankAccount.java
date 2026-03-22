package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.rule.CounterpartyBankAccountRules;
import com.cloudcontrol.catalog.domain.counterparty.vo.Bic;
import com.cloudcontrol.catalog.domain.counterparty.vo.Iban;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CounterpartyBankAccount implements Entity<Counterparty, CounterpartyBankAccount.CounterpartyBankAccountId> {

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

    static @NonNull CounterpartyBankAccount create(
            @NonNull String name,
            @Nullable String nameEn,
            @NonNull Bic bic,
            @NonNull Iban iban,
            boolean isPrimary
    ) {
        validateName(name);
        validateNameEn(nameEn);

        return new CounterpartyBankAccount(null, name, nameEn, bic, iban, isPrimary);
    }

    void updateName(@NonNull String name) {
        validateName(name);
        this.name = name;
    }

    void updateNameEn(@NonNull String nameEn) {
        validateNameEn(nameEn);
        this.nameEn = nameEn;
    }

    void removeNameEn() {
        this.nameEn = null;
    }

    void updateBic(@NonNull Bic bic) {
        this.bic = bic;
    }

    void updateIban(@NonNull Iban iban) {
        this.iban = iban;
    }

    void markAsPrimary() {
        this.isPrimary = true;
    }

    void unmarkAsPrimary() {
        this.isPrimary = false;
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
}
