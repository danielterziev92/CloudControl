package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Validation constraints and message codes for UIC (ЕИК) value objects.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class UicRules {

    /**
     * Rules specific to Bulgarian UIC.
     */
    @NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
    public static final class Bulgarian {

        /**
         * Valid lengths: 9 digits (legal entity) or 13 digits (branch).
         */
        public static final int LENGTH_STANDARD = 9;
        public static final int LENGTH_BRANCH = 13;

        /**
         * Compiled regex pattern — thread-safe, evaluated once at a class load.
         * Matches exactly 9 or exactly 13 digit strings.
         */
        public static final Pattern PATTERN = Pattern.compile("\\d{9}|\\d{13}");

        /**
         * Thrown when the raw value is null or blank. Maps to HTTP 422.
         */
        public static final String BLANK = "uic.blank";


        /**
         * Thrown when the value does not match {@link #PATTERN}. Maps to HTTP 422.
         */
        public static final String INVALID = "uic.invalid";

        /**
         * ISO 3166-1 alpha-2 country code.
         */
        public static final String COUNTRY_CODE = "BG";
    }
}
