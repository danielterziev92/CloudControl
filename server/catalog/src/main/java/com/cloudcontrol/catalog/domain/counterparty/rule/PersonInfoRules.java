package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Validation constraints and message codes for {@link com.cloudcontrol.catalog.domain.counterparty.PersonInfo}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonInfoRules {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FirstName {
        public static final int MAX_LENGTH = 50;

        public static final String BLANK = "person.first_name.blank";
        public static final String TOO_LONG = "person.first_name.too_long";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class MiddleName {
        public static final int MAX_LENGTH = 50;

        public static final String TOO_LONG = "person.middle_name.too_long";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class LastName {
        public static final int MAX_LENGTH = 50;

        public static final String BLANK = "person.last_name.blank";
        public static final String TOO_LONG = "person.last_name.too_long";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Type {
        public static final int MAX_LENGTH = 50;

        public static final String BLANK = "person.type.blank";
        public static final String INVALID = "person.type.invalid";
    }
}
