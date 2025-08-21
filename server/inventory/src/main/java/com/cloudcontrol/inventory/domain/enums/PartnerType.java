package com.cloudcontrol.inventory.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PartnerType {
    UNIVERSAL("Universal", true),
    SUPPLIER("Supplier", false),
    CLIENT("Client", false);

    public static final int MAX_LENGTH = 9;

    private final String displayName;
    private final boolean isDefault;

    PartnerType(String displayName, boolean isDefault) {
        this.displayName = displayName;
        this.isDefault = isDefault;
    }

    public static PartnerType getDefault() {
        return Arrays.stream(values())
                .filter(type -> type.isDefault)
                .findFirst()
                .orElse(UNIVERSAL);
    }
}