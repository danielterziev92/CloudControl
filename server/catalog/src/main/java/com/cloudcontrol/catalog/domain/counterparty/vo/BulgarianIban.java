package com.cloudcontrol.catalog.domain.counterparty.vo;

import com.cloudcontrol.catalog.domain.counterparty.rule.IbanRules;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import org.jspecify.annotations.NonNull;

/**
 * Bulgarian IBAN.
 *
 * @see com.cloudcontrol.catalog.domain.counterparty.rule.IbanRules.Bulgarian for format rules and constraints.
 */
public record BulgarianIban(String value) implements BankAccountNumber {

    public BulgarianIban {
        if (value == null || value.isBlank())
            throw new InvalidValueException(IbanRules.Bulgarian.BLANK);

        String normalized = value.replaceAll("\\s", "").toUpperCase();
        if (!IbanRules.Bulgarian.PATTERN.matcher(normalized).matches())
            throw new InvalidValueException(IbanRules.Bulgarian.INVALID, value);
    }

    @Override
    public @NonNull String countryCode() {
        return IbanRules.Bulgarian.COUNTRY_CODE;
    }

    @Override
    public @NonNull String normalized() {
        return value.replaceAll("\\s", "").toUpperCase();
    }
}
