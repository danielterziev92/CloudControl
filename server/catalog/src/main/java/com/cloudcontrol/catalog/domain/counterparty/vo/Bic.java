package com.cloudcontrol.catalog.domain.counterparty.vo;

import com.cloudcontrol.catalog.domain.counterparty.rule.BicRules;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import org.jmolecules.ddd.types.ValueObject;
import org.jspecify.annotations.NonNull;


/**
 * Bank Identifier Code (BIC), also known as SWIFT code, defined by ISO 9362.
 *
 * <p>Input is normalized to uppercase before validation,
 * so both {@code bpbibgsf} and {@code BPBIBGSF} are accepted.</p>
 *
 * @see com.cloudcontrol.catalog.domain.counterparty.rule.BicRules.Bic for format rules and constraints.
 */
public record Bic(String value) implements ValueObject {

    public Bic {
        if (value == null || value.isBlank())
            throw new InvalidValueException(BicRules.Bic.BLANK);

        String normalized = value.toUpperCase();
        if (!BicRules.Bic.PATTERN.matcher(normalized).matches())
            throw new InvalidValueException(BicRules.Bic.INVALID, value);

        value = normalized;
    }

    /**
     * Returns the 4-letter institution code (first 4 characters).
     */
    public @NonNull String institutionCode() {
        return value.substring(0, 4);
    }

    /**
     * Returns the ISO 3166-1 alpha-2 country code (characters 5-6).
     */
    public @NonNull String countryCode() {
        return value.substring(4, 6);
    }

    /**
     * Returns {@code true} if this is a BIC11 (includes branch code).
     */
    public boolean hasBranchCode() {
        return value.length() == BicRules.Bic.LENGTH;
    }

    /**
     * Returns the branch code if present, otherwise empty string.
     */
    public @NonNull String branchCode() {
        return hasBranchCode() ? value.substring(8) : "";
    }
}
