package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.rule.CounterpartyEmailRules;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;
import org.jspecify.annotations.NonNull;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CounterpartyEmailAddress implements Entity<Counterparty, CounterpartyEmailAddress.CounterpartyEmailAddressId> {

    public record CounterpartyEmailAddressId(Long value) implements Identifier {
    }

    private final CounterpartyEmailAddressId id;
    @NonNull
    private String email;
    private boolean isPrimary;

    static @NonNull CounterpartyEmailAddress create(@NonNull String email, boolean isPrimary) {
        validateEmail(email);
        return new CounterpartyEmailAddress(null, email, isPrimary);
    }

    void updateEmail(@NonNull String email) {
        validateEmail(email);
        this.email = email;
    }

    void markAsPrimary() {
        this.isPrimary = true;
    }

    void unmarkAsPrimary() {
        this.isPrimary = false;
    }

    private static void validateEmail(@NonNull String value) {
        if (value.isBlank())
            throw new InvalidValueException(CounterpartyEmailRules.Email.BLANK);

        if (value.length() > CounterpartyEmailRules.Email.MAX_LENGTH)
            throw new InvalidValueException(CounterpartyEmailRules.Email.TOO_LONG, CounterpartyEmailRules.Email.MAX_LENGTH);

        if (!CounterpartyEmailRules.Email.PATTERN.matcher(value).matches())
            throw new InvalidValueException(CounterpartyEmailRules.Email.INVALID, value);
    }
}
