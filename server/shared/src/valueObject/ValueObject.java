package valueObject;

import exceptions.DomainException;

import java.io.Serializable;

public interface ValueObject<T> extends Serializable {

    /**
     * Validates the value object's state.
     *
     * @throws DomainException if validation fails
     */
    void validate(T value);
}
