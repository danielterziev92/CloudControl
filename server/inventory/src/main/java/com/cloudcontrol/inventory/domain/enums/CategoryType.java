package com.cloudcontrol.inventory.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CategoryType {
    PRODUCT(1, "Product", true),
    PARTNER(2, "Partner", false);

    private final int code;
    private final String displayName;
    private final boolean isDefault;

    CategoryType(int code, String displayName, boolean isDefault) {
        this.code = code;
        this.displayName = displayName;
        this.isDefault = isDefault;
    }

    public static CategoryType getDefault() {
        return Arrays.stream(values())
                .filter(type -> type.isDefault)
                .findFirst()
                .orElse(PRODUCT);
    }

    public static CategoryType fromCode(int code) {
        return Arrays.stream(values())
                .filter(type -> type.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown CategoryType code: " + code));
    }
}
