package com.cloudcontrol.catalog.domain.shared.entities;

import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for all Entities in the domain.
 *
 * @param <ID>
 */
@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Entity<ID extends Serializable> {
    public static final String ID_NOT_NULL_MESSAGE = "Entity ID cannot be null";

    @Setter(AccessLevel.PROTECTED)
    protected ID id;

    protected Entity(ID id) {
        this.id = Objects.requireNonNull(id, ID_NOT_NULL_MESSAGE);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Entity<?> other = (Entity<?>) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
