package com.cloudcontrol.catalog.domain.counterparty.vo;

import com.cloudcontrol.catalog.domain.counterparty.rule.VatRules;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import org.jspecify.annotations.NonNull;

/**
 * Bulgarian VAT number (ДДС номер).
 *
 * @see com.cloudcontrol.catalog.domain.counterparty.rule.VatRules.Bulgarian for format rules and constraints.
 */
public record BulgarianVat(String value) implements TaxNumber {

    public BulgarianVat {
        if (value == null || value.isBlank())
            throw new InvalidValueException(VatRules.Bulgarian.BLANK);

        String normalized = value.trim().toUpperCase();
        if (!VatRules.Bulgarian.PATTERN.matcher(normalized).matches())
            throw new InvalidValueException(VatRules.Bulgarian.INVALID, value);
    }

    @Override
    public @NonNull String countryCode() {
        return VatRules.Bulgarian.COUNTRY_CODE;
    }
}
