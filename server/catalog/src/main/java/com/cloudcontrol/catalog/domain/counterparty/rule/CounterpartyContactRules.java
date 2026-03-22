package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Validation constraints and message codes for
 * {@link com.cloudcontrol.catalog.domain.counterparty.CounterpartyContact}.
 *
 * <p>Phone number validation is handled by the {@code PhoneNumber} Value Object
 * and its country-specific implementations (e.g. {@code BulgarianPhoneNumber}).</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CounterpartyContactRules {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class PersonName {
        public static final int MAX_LENGTH = 100;

        /**
         * Thrown when the value exceeds {@link #MAX_LENGTH}. Maps to HTTP 422.
         */
        public static final String TOO_LONG = "contact.person_name.too_long";
    }
}
