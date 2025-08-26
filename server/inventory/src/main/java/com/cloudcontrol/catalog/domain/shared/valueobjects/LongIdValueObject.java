package com.cloudcontrol.catalog.domain.shared.valueobjects;

import com.cloudcontrol.catalog.domain.shared.exceptions.DomainException;
import io.hypersistence.tsid.TSID;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for Long-based ID Value Objects.
 */
public interface LongIdValueObject extends ValueObject {
    @NotNull Long value();

    /**
     * Validates ID with automatic entity name extraction from the class name.
     */
    default void validateId(@NotNull Long value) {
        validateId(value, extractEntityName());
    }

    /**
     * Validates the ID value according to domain rules:
     * - Must not be null
     * - Must be positive
     * - Must not exceed maximum allowed value (Long.MAX_VALUE - 1000)
     *
     * @param value      The ID value to validate
     * @param entityName The name of the entity for error messages
     * @throws DomainException if validation fails
     */
    default void validateId(Long value, String entityName) {
        if (value == null) throw new DomainException(entityName + " id is required");

        if (value <= 0) throw new DomainException(entityName + " ID must be positive, was: " + value);

        if (value > Long.MAX_VALUE - 1000) throw new DomainException(entityName + " ID exceeds maximum allowed value");
    }


    /**
     * Extracts the entity name from the implementing class name.
     * Removes common ID-related suffixes like "Id", "ID", or "Identifier".
     * For example:
     * - "ProductId" becomes "Product"
     * - "OrderIdentifier" becomes "Order"
     * If no known suffix is found, returns the original class name.
     *
     * @return The extracted entity name as a String
     */
    default String extractEntityName() {
        String className = getClass().getSimpleName();

        if (className.endsWith("Id") || className.endsWith("ID")) {
            return className.substring(0, className.length() - 2);
        }

        String identifierString = "Identifier";

        if (className.endsWith(identifierString)) {
            return className.substring(0, className.length() - identifierString.length());
        }

        return className;
    }

    /**
     * Generates a unique time-sorted identifier as a Long value.
     * Uses TSID (Time-Sorted Unique Identifier) to ensure uniqueness and
     * sequential ordering based on creation time.
     *
     * @return A new unique Long identifier value
     * @throws RuntimeException if TSID generation fails
     */
    @NotNull
    @Contract(" -> new")
    static Long generateValue() {
        return TSID.Factory.getTsid().toLong();
    }
}
