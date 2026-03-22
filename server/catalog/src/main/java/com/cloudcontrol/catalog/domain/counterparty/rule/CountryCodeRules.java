package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Validation constraints for ISO 3166-1 alpha-2 country codes.
 *
 * <h3>What is ISO 3166-1 alpha-2?</h3>
 * A two-letter country code defined by the International Organization for
 * Standardization. Used across all international standards including IBAN,
 * BIC, phone numbers, and addresses.
 *
 * <h3>Format</h3>
 * Exactly 2 uppercase ASCII letters. Examples: {@code BG}, {@code DE}, {@code GB}.
 *
 * <p>Input is normalized to uppercase before validation,
 * so {@code bg}, {@code Bg} and {@code BG} are all accepted.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CountryCodeRules {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CountryCode {

        /**
         * ISO 3166-1 alpha-2 codes are always exactly 2 characters.
         */
        public static final int LENGTH = 2;

        /**
         * Compiled pattern — thread-safe, evaluated once at a class load.
         * Matches exactly 2 uppercase ASCII letters after normalization.
         */
        public static final Pattern PATTERN = Pattern.compile("^[A-Z]{2}$");

        /**
         * Thrown when the value is null or blank. Maps to HTTP 422.
         */
        public static final String BLANK = "country_code.blank";

        /**
         * Thrown when the value does not match {@link #PATTERN}. Maps to HTTP 422.
         */
        public static final String INVALID = "country_code.invalid";
    }
}
