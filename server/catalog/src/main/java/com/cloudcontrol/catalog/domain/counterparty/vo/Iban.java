package com.cloudcontrol.catalog.domain.counterparty.vo;

import org.jmolecules.ddd.types.ValueObject;

public record Iban(String value) implements ValueObject {

    public static final String REGEX = "[A-Z]{2}\\\\d{2}[A-Z0-9]{4,}";

    public Iban {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Iban cannot be null or blank");

        String normalized = value.replaceAll("\\s", "").toUpperCase();
        if (!normalized.matches(REGEX))
            throw new IllegalArgumentException("Iban must be a valid IBAN number");
    }

    public String normalized() {
        return this.value.replaceAll("\\s", "").toUpperCase();
    }
}
