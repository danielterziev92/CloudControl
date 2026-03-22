package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Validation constraints for IBAN (International Bank Account Number),
 * defined by ISO 13616-1:2020 and ISO 7064 (MOD 97-10).
 *
 * <h3>Structure</h3>
 * <pre>
 *   CC KK BBAN
 *   ││ ││ └── Basic Bank Account Number (country-specific length and format)
 *   ││ └──── Check digits (2 digits, calculated via MOD 97-10)
 *   └─────── Country code (ISO 3166-1 alpha-2, 2 letters)
 * </pre>
 *
 * <h3>MOD 97-10 validation algorithm (ISO 7064)</h3>
 * <ol>
 *   <li>Normalize: remove spaces, uppercase</li>
 *   <li>Rearrange: move the first 4 characters to the end</li>
 *   <li>Convert: replace each letter with its numeric value (A=10, B=11, … Z=35)</li>
 *   <li>Compute: calculate the resulting number modulo 97</li>
 *   <li>Valid if remainder == 1</li>
 * </ol>
 *
 * <p>Example: {@code BE62 5100 0754 7061}</p>
 * <ol>
 *   <li>Normalize  → {@code BE62510007547061}</li>
 *   <li>Rearrange  → {@code 510007547061BE62}</li>
 *   <li>Convert    → {@code 510007547061111162} (B=11, E=14)</li>
 *   <li>Mod 97     → {@code 510007547061111162 mod 97 = 1}</li>
 * </ol>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IbanRules {

    /**
     * Rules specific to Bulgarian IBAN.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Iban {

        /**
         * Maximum IBAN length across all countries (ISO 13616 allows up to 34).
         */
        public static final int MAX_LENGTH = 34;

        /**
         * Structural pattern — alphanumeric only, 2-letter country code prefix.
         * Length and MOD 97 checksum are validated separately.
         */
        public static final Pattern PATTERN = Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$");

        /**
         * Returns the expected IBAN length for the given ISO 3166-1 alpha-2 country code,
         * or {@code -1} if the country is not in the ISO 13616 registry.
         *
         * <p>Uses a {@code switch} expression — compiled to a {@code hashCode()} lookup,
         * O(1), no Map initialization overhead.</p>
         *
         * @param countryCode ISO 3166-1 alpha-2 code (e.g. {@code "BG"})
         * @return expected IBAN length, or {@code -1} for unknown countries
         */
        public static int expectedLength(String countryCode) {
            return switch (countryCode) {
                case "NO" -> 15;
                case "BE" -> 16;
                case "DK", "FI", "FO", "GL", "NL" -> 18;
                case "MK", "SI" -> 19;
                case "AT", "BA", "EE", "KZ", "LT", "LU", "XK" -> 20;
                case "CR", "HR", "LI", "LV", "CH" -> 21;
                case "AE", "BG", "BH", "DE", "GB",
                     "GE", "IE", "ME", "RS" -> 22;
                case "GI", "IL" -> 23;
                case "AD", "CZ", "MD", "PK", "RO", "SA",
                     "SE", "SK", "ES", "TN", "VG" -> 24;
                case "PT" -> 25;
                case "IS" -> 26;
                case "FR", "GR", "IT", "MC", "MR", "SM" -> 27;
                case "AL", "AZ", "CY", "DO", "GT",
                     "HU", "LB", "PL" -> 28;
                case "BR", "PS", "QA" -> 29;
                case "JO", "KW", "MU" -> 30;
                case "MT" -> 31;
                default -> -1;
            };
        }

        /**
         * Thrown when the value is null or blank. Maps to HTTP 422.
         */
        public static final String BLANK = "iban.blank";

        /**
         * Thrown when the value does not match the structural {@link #PATTERN}. Maps to HTTP 422.
         */
        public static final String INVALID_FORMAT = "iban.invalid_format";

        /**
         * Thrown when the country code is known, but the length is wrong. Maps to HTTP 422.
         */
        public static final String INVALID_LENGTH = "iban.invalid_length";

        /**
         * Thrown when MOD 97-10 checksum fails. Maps to HTTP 422.
         */
        public static final String INVALID_CHECKSUM = "iban.invalid_checksum";
    }
}
