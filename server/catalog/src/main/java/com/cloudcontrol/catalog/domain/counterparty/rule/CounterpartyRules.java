package com.cloudcontrol.catalog.domain.counterparty.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Business rule message codes for the {@link com.cloudcontrol.catalog.domain.counterparty.Counterparty} aggregate.
 * <p>
 * These are invariant violations (HTTP 409), as opposed to value object
 * validation errors (HTTP 422) which live in the entity-specific Rules classes.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CounterpartyRules {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Lifecycle {
        public static final String ALREADY_INACTIVE = "counterparty.already_inactive";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Type {
        public static final String ORGANIZATION_ONLY = "counterparty.organization_only";
    }
}
