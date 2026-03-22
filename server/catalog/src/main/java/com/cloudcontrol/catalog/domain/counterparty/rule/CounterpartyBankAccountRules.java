package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Validation constraints and message codes for
 * {@link com.cloudcontrol.catalog.domain.counterparty.CounterpartyBankAccount}.
 *
 * <p>BIC validation is handled by {@link BicRules}.</p>
 * <p>IBAN validation is handled by the {@code BankAccountNumber} Value Object.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CounterpartyBankAccountRules {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Name {
        public static final int MAX_LENGTH = 100;

        /**
         * Thrown when the value is null or blank. Maps to HTTP 422.
         */
        public static final String BLANK = "bank_account.name.blank";

        /**
         * Thrown when the value exceeds {@link #MAX_LENGTH}. Maps to HTTP 422.
         */
        public static final String TOO_LONG = "bank_account.name.too_long";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class NameEn {
        public static final int MAX_LENGTH = 100;

        /**
         * Thrown when the value exceeds {@link #MAX_LENGTH}. Maps to HTTP 422.
         */
        public static final String TOO_LONG = "bank_account.name_en.too_long";
    }
}
