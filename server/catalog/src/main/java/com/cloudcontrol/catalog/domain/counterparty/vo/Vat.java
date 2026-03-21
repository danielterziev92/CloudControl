package com.cloudcontrol.catalog.domain.counterparty.vo;

import org.jmolecules.ddd.types.ValueObject;

public record Vat(String value) implements ValueObject {

    public static final String REGEX = "BG\\\\d{9,10}";

    public Vat {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Vat cannot be null or blank");

        if (!value.matches(REGEX))
            throw new IllegalArgumentException("Vat must be a valid VAT number");
    }
}
