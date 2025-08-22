package com.cloudcontrol.inventory.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PartnerType {
    UNIVERSAL("Universal", true),
    SUPPLIER("Supplier", false),
    CLIENT("Client", false);

    private final String displayName;
    private final boolean isDefault;

    public static PartnerType getDefault() {
        return Arrays.stream(values())
                .filter(type -> type.isDefault)
                .findFirst()
                .orElse(UNIVERSAL);
    }
}