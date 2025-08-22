package com.cloudcontrol.inventory.domain.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ProductType {
    PHYSICAL("Standard", true),
    FIXED_PRICE("Fixed Price", false),
    SERVICE("Service", false),
    PAYMENT("Payment", false);

    private final String displayName;
    private final boolean isDefault;

    public static ProductType getDefault() {
        return Arrays.stream(values()).filter(type -> type.isDefault).findFirst().orElse(PHYSICAL);
    }
}