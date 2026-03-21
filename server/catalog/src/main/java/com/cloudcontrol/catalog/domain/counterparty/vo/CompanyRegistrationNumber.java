package com.cloudcontrol.catalog.domain.counterparty.vo;

import org.jmolecules.ddd.types.ValueObject;

/**
 * Abstracts over country-specific company registration numbers.
 * <p>
 * Sealed — only known implementations are permitted, giving us
 * exhaustive switch expressions when country-specific logic is needed.
 * <p>
 * Current implementations:
 * BG → BulgarianUic (ЕИК)
 */
public sealed interface CompanyRegistrationNumber extends ValueObject
        permits BulgarianUic {

    String value();

    String countryCode();
}
