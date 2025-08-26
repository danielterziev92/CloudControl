package com.cloudcontrol.catalog.domain.shared.exceptions;

/**
 * Base class for all domain exceptions.
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
