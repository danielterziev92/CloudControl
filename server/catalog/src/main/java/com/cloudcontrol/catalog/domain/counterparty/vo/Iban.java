package com.cloudcontrol.catalog.domain.counterparty.vo;

import com.cloudcontrol.catalog.domain.counterparty.rule.IbanRules;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import org.jmolecules.ddd.types.ValueObject;
import org.jspecify.annotations.NonNull;

import java.math.BigInteger;

/**
 * International Bank Account Number (IBAN), defined by ISO 13616-1:2020.
 *
 * <p>Validates:</p>
 * <ol>
 *   <li>Structural format — 2-letter country code + 2 check digits + BBAN</li>
 *   <li>Country-specific length — via {@link IbanRules.Iban#expectedLength(String)}</li>
 *   <li>MOD 97-10 checksum — per ISO 7064</li>
 * </ol>
 *
 * <p>Input is normalized (spaces removed, uppercased) before validation,
 * so both {@code BG80 BNBG 9661 1020 3456 78} and {@code BG80BNBG96611020345678}
 * are accepted.</p>
 *
 * @see com.cloudcontrol.catalog.domain.counterparty.rule.IbanRules.Iban for validation constants and country lengths.
 */
public record Iban(String value) implements ValueObject {

    public Iban {
        if (value == null || value.isBlank())
            throw new InvalidValueException(IbanRules.Iban.BLANK);

        String normalized = normalize(value);

        if (!IbanRules.Iban.PATTERN.matcher(normalized).matches())
            throw new InvalidValueException(IbanRules.Iban.INVALID_FORMAT, value);

        String countryCode = normalized.substring(0, 2);
        int expectedLength = IbanRules.Iban.expectedLength(countryCode);
        if (expectedLength != -1 && normalized.length() != expectedLength)
            throw new InvalidValueException(IbanRules.Iban.INVALID_LENGTH, countryCode, expectedLength, normalized.length());

        if (!isValidMod97(normalized))
            throw new InvalidValueException(IbanRules.Iban.INVALID_CHECKSUM, value);

        value = normalized;
    }

    /**
     * Returns the IBAN in printed format with spaces every 4 characters.
     */
    public @NonNull String formatted() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            if (i > 0 && i % 4 == 0) sb.append(' ');
            sb.append(value.charAt(i));
        }
        return sb.toString();
    }

    /**
     * Returns the ISO 3166-1 alpha-2 country code embedded in this IBAN.
     */
    public @NonNull String countryCode() {
        return value.substring(0, 2);
    }

    private static @NonNull String normalize(@NonNull String raw) {
        return raw.replaceAll("\\s", "").toUpperCase();
    }

    /**
     * MOD 97-10 algorithm per ISO 7064 / ISO 13616:
     * 1. Move the first 4 chars to the end
     * 2. Replace each letter with its numeric value (A=10 … Z=35)
     * 3. Compute mod 97 — valid if the remainder == 1
     */
    private static boolean isValidMod97(@NonNull String iban) {
        String rearranged = iban.substring(4) + iban.substring(0, 4);

        StringBuilder numeric = new StringBuilder();
        for (char c : rearranged.toCharArray()) {
            if (Character.isLetter(c))
                numeric.append(c - 'A' + 10);
            else
                numeric.append(c);
        }

        return new BigInteger(numeric.toString()).mod(BigInteger.valueOf(97)).intValue() == 1;
    }
}
