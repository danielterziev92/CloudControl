package com.cloudcontrol.catalog.domain.counterparty.vo;

import com.cloudcontrol.catalog.domain.counterparty.rule.UicRules;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;

import java.util.regex.Pattern;

/**
 * Bulgarian company registration number (ЕИК / Единен идентификационен код).
 *
 * @see UicRules.Bulgarian for format rules and message codes
 */
public record BulgarianUic(String value) implements CompanyRegistrationNumber {

    public BulgarianUic {
        if (value == null || value.isBlank())
            throw new InvalidValueException(UicRules.Bulgarian.BLANK);

        if (!UicRules.Bulgarian.PATTERN.matcher(value).matches())
            throw new InvalidValueException(UicRules.Bulgarian.INVALID, value);
    }

    @Override
    public String countryCode() {
        return UicRules.Bulgarian.COUNTRY_CODE;
    }
}