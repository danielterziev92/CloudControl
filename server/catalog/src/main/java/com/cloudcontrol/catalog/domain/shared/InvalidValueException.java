package com.cloudcontrol.catalog.domain.shared;

/**
 * Thrown when a Value Object receives an invalid input.
 * Maps to HTTP 422 Unprocessable Entity.
 * <p>
 * Examples: malformed UIC, invalid IBAN format, blank required field.
 */
public class InvalidValueException extends DomainException {
    public InvalidValueException(String messageCode, Object... args) {
        super(messageCode, args);
    }
}
