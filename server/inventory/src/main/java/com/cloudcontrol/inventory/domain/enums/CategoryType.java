package com.cloudcontrol.inventory.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {
    PRODUCT("Product", true),
    PARTNER("Partner", false);

    private final String displayName;
    private final boolean isDefault;
}
