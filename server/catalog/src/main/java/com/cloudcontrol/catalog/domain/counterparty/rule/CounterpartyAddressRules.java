package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Validation constraints and message codes for
 * {@link com.cloudcontrol.catalog.domain.counterparty.CounterpartyAddress}.
 *
 * <p>Country validation is handled by the
 * {@link com.cloudcontrol.catalog.domain.counterparty.vo.CountryCode} Value Object.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CounterpartyAddressRules {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Language {

        /**
         * Thrown when language is null. Maps to HTTP 422.
         */
        public static final String NULL = "address.language.null";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Region {

        public static final int MAX_LENGTH = 100;

        /**
         * Thrown when the value exceeds {@link #MAX_LENGTH}. Maps to HTTP 422.
         */
        public static final String TOO_LONG = "address.region.too_long";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Town {

        public static final int MAX_LENGTH = 100;

        /**
         * Thrown when the value is null or blank. Maps to HTTP 422.
         */
        public static final String BLANK = "address.town.blank";

        /**
         * Thrown when the value exceeds {@link #MAX_LENGTH}. Maps to HTTP 422.
         */
        public static final String TOO_LONG = "address.town.too_long";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Address {

        public static final int MAX_LENGTH = 255;

        /**
         * Thrown when the value is null or blank. Maps to HTTP 422.
         */
        public static final String BLANK = "address.address.blank";

        /**
         * Thrown when the value exceeds {@link #MAX_LENGTH}. Maps to HTTP 422.
         */
        public static final String TOO_LONG = "address.address.too_long";
    }
}
