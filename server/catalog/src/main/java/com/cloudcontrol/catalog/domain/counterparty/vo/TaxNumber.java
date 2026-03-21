package com.cloudcontrol.catalog.domain.counterparty.vo;

import org.jmolecules.ddd.types.ValueObject;

/**
 * Abstracts over country-specific tax / VAT numbers.
 * <p>
 * Sealed — only known implementations are permitted.
 * <p>
 * Current implementations:
 * BG → BulgarianVat (ДДС номер, prefix "BG")
 */
public sealed interface TaxNumber extends ValueObject
        permits BulgarianVat {

    String value();

    String countryCode();
}
