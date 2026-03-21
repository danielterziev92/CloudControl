package com.cloudcontrol.catalog.domain.counterparty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CounterpartyEmailAddress implements Entity<Counterparty, CounterpartyEmailAddress.CounterpartyEmailAddressId> {

    public record CounterpartyEmailAddressId(Long value) implements Identifier {
    }

    private static final String EMAIL_PATTERN = "^[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

    private final CounterpartyEmailAddressId id;
    private final String email;
    private boolean isPrimary;

    static CounterpartyEmailAddress create(String email, boolean isPrimary) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!email.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        return new CounterpartyEmailAddress(null, email, isPrimary);
    }

    void markAsPrimary() {
        this.isPrimary = true;
    }

    void unmarkAsPrimary() {
        this.isPrimary = false;
    }
}
