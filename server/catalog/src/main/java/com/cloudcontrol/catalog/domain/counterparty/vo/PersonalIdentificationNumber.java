package com.cloudcontrol.catalog.domain.counterparty.vo;

import org.jmolecules.ddd.types.ValueObject;

public sealed interface PersonalIdentificationNumber extends ValueObject
        permits BulgarianPin {

    String value();

    String countryCode();

    boolean isAnonymous();
}
