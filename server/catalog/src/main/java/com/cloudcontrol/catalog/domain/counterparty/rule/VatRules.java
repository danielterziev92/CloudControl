package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Validation constraints and message codes for VAT number value objects.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class VatRules {

    /**
     * Rules specific to Bulgarian VAT.
     */
    @NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
    public static final class Bulgarian {

        /**
         * ISO 3166-1 alpha-2 country code.
         */
        public static final String COUNTRY_CODE = "BG";

        /**
         * Minimum total length: "BG" + 9 digits.
         */
        public static final int MIN_LENGTH = 11;

        /**
         * Maximum total length: "BG" + 10 digits.
         */
        public static final int MAX_LENGTH = 12;

        /**
         * Compiled regex pattern — thread-safe, evaluated once at a class load.
         * Matches "BG" followed by 9 or 10 digits (case-sensitive after normalization).
         */
        public static final Pattern PATTERN = Pattern.compile("BG\\d{9,10}");

        /**
         * Thrown when the raw value is null or blank. Maps to HTTP 422.
         */
        public static final String BLANK = "vat.blank";

        /**
         * Thrown when the value does not match {@link #PATTERN}. Maps to HTTP 422.
         */
        public static final String INVALID = "vat.invalid";
    }
}
