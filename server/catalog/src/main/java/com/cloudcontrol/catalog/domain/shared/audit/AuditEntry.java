package com.cloudcontrol.catalog.domain.shared.audit;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

/**
 * Immutable record of a single field-level change on a domain entity.
 *
 * <p>One {@code AuditEntry} is created per changed field per operation.</p>
 *
 * <h3>Convention by action</h3>
 * <ul>
 *   <li>{@link AuditAction#CREATE} — {@code oldValue} is {@code null}</li>
 *   <li>{@link AuditAction#UPDATE} — both values present</li>
 *   <li>{@link AuditAction#DELETE} — {@code newValue} is {@code null}</li>
 * </ul>
 */
public record AuditEntry(
        @NonNull String entityType,
        @NonNull String entityId,
        @NonNull String field,
        @NonNull AuditAction action,
        @Nullable String oldValue,
        @Nullable String newValue,
        @NonNull UserIdentifier changedBy,
        @NonNull Instant occurredAt
) {

    /**
     * Factory for CREATE — oldValue is always null.
     */
    public static @NonNull AuditEntry created(
            @NonNull String entityType,
            @NonNull String entityId,
            @NonNull String field,
            @Nullable String newValue,
            @NonNull UserIdentifier changedBy
    ) {
        return new AuditEntry(entityType, entityId, field, AuditAction.CREATE, null, newValue, changedBy, Instant.now());
    }

    /**
     * Factory for UPDATE — both old and new values are present.
     */
    public static @NonNull AuditEntry updated(
            @NonNull String entityType,
            @NonNull String entityId,
            @NonNull String field,
            @Nullable String oldValue,
            @Nullable String newValue,
            @NonNull UserIdentifier changedBy
    ) {
        return new AuditEntry(entityType, entityId, field, AuditAction.UPDATE, oldValue, newValue, changedBy, Instant.now());
    }

    /**
     * Factory for DELETE — newValue is always null.
     */
    public static @NonNull AuditEntry deleted(
            @NonNull String entityType,
            @NonNull String entityId,
            @NonNull String field,
            @Nullable String oldValue,
            @NonNull UserIdentifier changedBy
    ) {
        return new AuditEntry(entityType, entityId, field, AuditAction.DELETE, oldValue, null, changedBy, Instant.now());
    }
}
