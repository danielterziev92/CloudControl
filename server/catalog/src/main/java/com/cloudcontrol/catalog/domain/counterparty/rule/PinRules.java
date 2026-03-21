package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Validation constraints and message codes for Bulgarian PIN (ЕГН) value objects.
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PinRules {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Bulgarian {

        /**
         * Bulgarian ЕГН is always exactly 10 digits.
         */
        public static final int LENGTH = 10;

        /**
         * Month offset for persons born in 1800–1899.
         * The raw month value in ЕГН will be 21–32.
         */
        public static final int MONTH_OFFSET_1800 = 20;

        /**
         * Month offset for persons born in 2000–2099.
         * The raw month value in ЕГН will be 41–52.
         */
        public static final int MONTH_OFFSET_2000 = 40;

        /**
         * Sentinel value used when the person declines to provide their ЕГН.
         * Bypasses all validation in {@code BulgarianPin}.
         */
        public static final String ANONYMOUS_VALUE = "9999999999";

        /**
         * Thrown when the raw value is null or blank. Maps to HTTP 422.
         */
        public static final String BLANK = "pin.blank";

        /**
         * Thrown when the value is not exactly 10 digits. Maps to HTTP 422.
         */
        public static final String INVALID_FORMAT = "pin.invalid_format";

        /**
         * ISO 3166-1 alpha-2 country code.
         */
        public static final String COUNTRY_CODE = "BG";
    }
}
