package com.cloudcontrol.catalog.domain.shared;

/**
 * Thrown when an Aggregate or Entity lookup returns no result.
 * Maps to HTTP 404 Not Found.
 * <p>
 * Example: a counterparty with the given ID does not exist.
 */
public class EntityNotFoundException extends DomainException {

    public EntityNotFoundException(String messageCode, Object... args) {
        super(messageCode, args);
    }
}
