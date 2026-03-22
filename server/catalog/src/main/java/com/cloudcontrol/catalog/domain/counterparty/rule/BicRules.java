package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Validation constraints and message codes for BIC (Business Identifier Code) / SWIFT codes.
 *
 * <h3>What is a BIC?</h3>
 * A BIC (ISO 9362) uniquely identifies a financial institution globally.
 * Also known as SWIFT code, SWIFT-BIC, or SWIFT ID.
 *
 * <h3>Structure</h3>
 * <pre>
 *   AAAA  BB  CC  [DDD]
 *   ^^^^  ^^  ^^  ^^^^^
 *   |     |   |   Branch code (optional, 3 alphanumeric) — omitted = head office
 *   |     |   Location code (2 alphanumeric)
 *   |     ISO 3166-1 alpha-2 country code (2 letters)
 *   Institution code (4 letters)
 * </pre>
 *
 * <ul>
 *   <li>BIC8 — 8 characters, identifies head office: {@code BNPAFRPP}</li>
 *   <li>BIC11 — 11 characters, identifies branch:     {@code BNPAFRPPXXX}</li>
 * </ul>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BicRules {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Bic {

        public static final int LENGTH = 11;

        /**
         * Compiled ISO 9362 pattern — thread-safe, evaluated once at a class load.
         * Matches BIC8 (8 chars) or BIC11 (11 chars), uppercase only.
         * Input should be normalized to uppercase before matching.
         */
        public static final Pattern PATTERN = Pattern.compile("^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$");

        /**
         * Thrown when the value is null or blank. Maps to HTTP 422.
         */
        public static final String BLANK = "bic.blank";

        /**
         * Thrown when the value does not match {@link #PATTERN}. Maps to HTTP 422.
         */
        public static final String INVALID = "bic.invalid";
    }
}
