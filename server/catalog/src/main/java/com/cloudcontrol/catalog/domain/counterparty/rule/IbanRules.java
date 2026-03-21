package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Validation constraints and message codes for IBAN value objects.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IbanRules {

    /**
     * Rules specific to Bulgarian IBAN.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Bulgarian {

        /**
         * ISO 3166-1 alpha-2 country code.
         */
        public static final String COUNTRY_CODE = "BG";

        /**
         * Bulgarian IBAN is always exactly 22 characters.
         */
        public static final int LENGTH = 22;

        /**
         * Compiled regex pattern — thread-safe, evaluated once at a class load.
         * Structure: BG + 2 check digits + 4 alphas (bank) + 4 digits (branch) + 8 digits (account).
         */
        public static final Pattern PATTERN = Pattern.compile("BG\\d{2}[A-Z]{4}\\d{4}\\d{8}");

        /**
         * Thrown when the raw value is null or blank. Maps to HTTP 422.
         */
        public static final String BLANK = "iban.blank";

        /**
         * Thrown when the value does not match {@link #PATTERN}. Maps to HTTP 422.
         */
        public static final String INVALID = "iban.invalid";
    }
}
