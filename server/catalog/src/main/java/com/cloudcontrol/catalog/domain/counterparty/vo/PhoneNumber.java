package com.cloudcontrol.catalog.domain.counterparty.vo;

import org.jmolecules.ddd.types.ValueObject;

/**
 * Abstracts over country-specific phone number formats.
 *
 * <p>Each implementation handles local format normalization to E.164,
 * so consumers always work with a consistent {@link #e164()} value
 * regardless of what the user originally entered.</p>
 *
 * <p>Sealed — only known implementations are permitted.</p>
 * <p>
 * Current implementations:
 * BG → {@link BulgarianPhoneNumber}
 */
public sealed interface PhoneNumber extends ValueObject
        permits BulgarianPhoneNumber {

    /**
     * The raw value as entered by the user.
     */
    String value();

    /**
     * The normalized E.164 representation (e.g. {@code +359888123456}).
     */
    String e164();

    /**
     * ISO 3166-1 alpha-2 country code.
     */
    String countryCode();
}
