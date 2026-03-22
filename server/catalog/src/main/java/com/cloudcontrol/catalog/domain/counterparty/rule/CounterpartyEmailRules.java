package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Validation constraints and message codes for
 * {@link com.cloudcontrol.catalog.domain.counterparty.CounterpartyEmailAddress}.
 *
 * <h3>Length</h3>
 * RFC 5321 defines a maximum of 254 characters for the full email address.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class CounterpartyEmailRules {

    @NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
    public static final class Email {
        /**
         * RFC 5321 maximum length for a full email address.
         */
        public static final int MAX_LENGTH = 254;

        /**
         * Compiled regex pattern — thread-safe, evaluated once at a class load.
         * Covers the practical subset of valid email addresses.
         */
        public static final Pattern PATTERN = Pattern.compile("^[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

        /**
         * Thrown when the value is null or blank. Maps to HTTP 422.
         */
        public static final String BLANK = "email.blank";

        /**
         * Thrown when the value does not match {@link #PATTERN}. Maps to HTTP 422.
         */
        public static final String INVALID = "email.invalid";

        /**
         * Thrown when the value exceeds {@link #MAX_LENGTH}. Maps to HTTP 422.
         */
        public static final String TOO_LONG = "email.too_long";
    }
}
