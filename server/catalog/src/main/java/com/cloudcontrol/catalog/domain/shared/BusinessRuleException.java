package com.cloudcontrol.catalog.domain.shared;

/**
 * Thrown when a domain invariant or business rule is violated.
 * Maps to HTTP 409 Conflict.
 * <p>
 * Examples: deactivating an already-inactive counterparty,
 * assigning VAT to a Person counterparty.
 */
public class BusinessRuleException extends DomainException {
    public BusinessRuleException(String messageCode, Object... args) {
        super(messageCode, args);
    }
}
