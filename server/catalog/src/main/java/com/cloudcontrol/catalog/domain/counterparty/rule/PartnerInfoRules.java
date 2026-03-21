package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Validation constraints and message codes for {@link com.cloudcontrol.catalog.domain.counterparty.PartnerInfo}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PartnerInfoRules {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Name {
        public static final int MAX_LENGTH = 100;
        public static final String BLANK = "partner.name.blank";
        public static final String TOO_LONG = "partner.name.too_long";
    }

    public static final class NameEn {
        public static final int MAX_LENGTH = 100;
        public static final String TOO_LONG = "partner.name_en.too_long";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Uic {
        public static final String BLANK = "uic.blank";
        public static final String INVALID = "uic.invalid";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Vat {
        public static final String BLANK = "vat.blank";
        public static final String INVALID = "vat.invalid";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Custodian {
        public static final int MAX_LENGTH = 100;
        public static final String BLANK = "partner.custodian.blank";
        public static final String TOO_LONG = "partner.custodian.too_long";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CustodianEn {
        public static final int MAX_LENGTH = 100;
        public static final String TOO_LONG = "partner.custodian_en.too_long";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Type {
        public static final int MAX_LENGTH = 50;
        public static final String BLANK = "partner.type.blank";
        public static final String INVALID = "partner.type.invalid";
    }
}
