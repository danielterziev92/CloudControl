package com.cloudcontrol.catalog.domain.counterparty.vo;

import com.cloudcontrol.catalog.domain.counterparty.rule.CountryCodeRules;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import org.jmolecules.ddd.types.ValueObject;
import org.jspecify.annotations.NonNull;

/**
 * ISO 3166-1 alpha-2 country code.
 *
 * <p>Shared Value Object — used across multiple bounded contexts:
 * addresses, IBAN, BIC, phone numbers.</p>
 *
 * <p>Input is normalized to uppercase on construction,
 * so {@code bg}, {@code Bg} and {@code BG} all produce {@code CountryCode("BG")}.</p>
 *
 * @see com.cloudcontrol.catalog.domain.counterparty.rule.CountryCodeRules.CountryCode for validation constants.
 */
public record CountryCode(String value) implements ValueObject {

    public CountryCode {
        if (value == null || value.isBlank())
            throw new InvalidValueException(CountryCodeRules.CountryCode.BLANK);

        String normalized = value.toUpperCase();
        if (!CountryCodeRules.CountryCode.PATTERN.matcher(normalized).matches())
            throw new InvalidValueException(CountryCodeRules.CountryCode.INVALID, value);

        value = normalized;
    }

    /**
     * Convenience factory — semantically clearer at call sites.
     */
    public static @NonNull CountryCode of(String value) {
        return new CountryCode(value);
    }
}
