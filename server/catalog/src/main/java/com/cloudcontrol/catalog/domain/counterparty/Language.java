package com.cloudcontrol.catalog.domain.counterparty;

/**
 * Supported languages for localized counterparty data (addresses, names).
 * Extracted as a top-level type, so other entities in the counterparty
 * context can reference it without coupling to CounterpartyAddress.
 */
public enum Language {
    EN,
    BG
}
