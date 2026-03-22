package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Validation constraints and message codes for Bulgarian phone numbers.
 *
 * <h3>Accepted formats</h3>
 * <ul>
 *   <li>Local:  {@code 08XXXXXXXX} — 10 digits, starts with {@code 08}</li>
 *   <li>E.164:  {@code +3598XXXXXXXX} — starts with {@code +359}, then 8/9 digit prefix</li>
 * </ul>
 *
 * <h3>Normalization</h3>
 * Both formats are normalized to E.164 on construction:
 * {@code 0888123456} → {@code +359888123456}
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PhoneNumberRules {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Bulgarian {
        public static final String COUNTRY_CODE = "BG";
        public static final String COUNTRY_PREFIX = "+359";

        /**
         * Compiled pattern for local format — {@code 0} + 9 digits.
         * The second digit must be non-zero (Bulgarian numbers start with 02, 03, 08, 09, etc.)
         */
        public static final Pattern LOCAL_PATTERN = Pattern.compile("^0[1-9]\\d{8}$");

        /**
         * Compiled pattern for E.164 format — {@code +359} + 9 digits.
         */
        public static final Pattern E164_PATTERN = Pattern.compile("^\\+359[1-9]\\d{8}$");

        /**
         * Thrown when the value is null or blank. Maps to HTTP 422.
         */
        public static final String BLANK = "phone.bg.blank";

        /**
         * Thrown when the value matches neither local nor E.164 format. Maps to HTTP 422.
         */
        public static final String INVALID = "phone.bg.invalid";
    }
}
