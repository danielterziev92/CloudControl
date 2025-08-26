package com.cloudcontrol.catalog.domain.shared.valueobjects;

import java.io.Serializable;

/**
 * Marker interface for Value Objects in Clean Architecture.
 * <p>
 * Value Objects should:
 * - Be immutable
 * - Validate their state upon creation
 * - Implement equality based on their properties
 * - Be serializable for persistence and messaging
 *
 * @see <a href="https://martinfowler.com/bliki/ValueObject.html">Martin Fowler - Value Object</a>
 */
public interface ValueObject extends Serializable {
}
