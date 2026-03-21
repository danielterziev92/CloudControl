package com.cloudcontrol.catalog.domain.shared;

import lombok.Getter;

/**
 * Base class for all domain exceptions.
 * Intentionally framework-agnostic — carries a message code and optional
 * interpolation args; the presentation layer is responsible for resolving
 * them to a localized string via MessageSource (or any other mechanism).
 */
@Getter
public abstract class DomainException extends RuntimeException {

    private final String messageCode;
    private final Object[] args;

    protected DomainException(String messageCode, Object... args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }
}
