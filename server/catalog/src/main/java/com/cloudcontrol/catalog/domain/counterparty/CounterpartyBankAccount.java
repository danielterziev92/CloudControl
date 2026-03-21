package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.vo.Iban;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;

public class CounterpartyBankAccount implements Entity<Counterparty, CounterpartyBankAccount.CounterpartyBankAccountId> {

    public record CounterpartyBankAccountId(Long value) implements Identifier {
    }

    private final CounterpartyBankAccountId id;
    private final String name;
    private final String bic;
    private final Iban iban;
    private boolean isPrimary;

    static CounterpartyBankAccount create(
            String name,
            String bic,
            Iban iban,
            boolean isPrimary
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Bank account name is required");
        }
        if (iban == null) {
            throw new IllegalArgumentException("IBAN is required");
        }
        return new CounterpartyBankAccount(null, name, bic, iban, isPrimary);
    }

    void markAsPrimary() {
        this.isPrimary = true;
    }

    void unmarkAsPrimary() {
        this.isPrimary = false;
    }
}
