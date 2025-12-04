package exceptions;


/**
 * Core domain exception used across the whole domain layer.
 *
 * <p>Represents business rule violations or invariant breaches raised by domain
 * services, aggregates, or value objects. Prefer creating specific subclasses
 * when callers need to handle different failure types; use this class for
 * generic domain errors that should propagate for logging or mapping to
 * API responses.</p>
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
