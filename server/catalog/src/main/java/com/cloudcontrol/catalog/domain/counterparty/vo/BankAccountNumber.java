package com.cloudcontrol.catalog.domain.counterparty.vo;

import org.jmolecules.ddd.types.ValueObject;

/**
 * Abstracts over country-specific bank account numbers.
 * <p>
 * Sealed — only known implementations are permitted.
 * <p>
 * Current implementations:
 * BG → BulgarianIban
 */
public sealed interface BankAccountNumber extends ValueObject
        permits BulgarianIban {

    String value();

    String countryCode();

    /**
     * Returns the value stripped of whitespace and uppercased.
     */
    String normalized();
}
