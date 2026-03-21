package com.cloudcontrol.catalog.domain.counterparty.vo;

import org.jmolecules.ddd.types.ValueObject;

public record Uic(String value) implements ValueObject {
    public static final String REGEX = "\\\\d{9}|\\\\d{13}";

    public Uic {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Uic cannot be null or blank");

        if (!value.matches(REGEX))
            throw new IllegalArgumentException("Uic must be a valid UIC number");
    }
}
