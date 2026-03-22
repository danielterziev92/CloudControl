package com.cloudcontrol.catalog.domain.shared.audit;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for creating {@link AuditEntry} records in bulk.
 *
 * <h3>Usage — CREATE</h3>
 * <pre>{@code
 * AuditBuilder.forCreate(entity(), entityId(), changedBy)
 *     .field(Fields.NAME,     name)
 *     .field(Fields.NAME_EN,  nameEn)
 *     .buildInto(this);
 * }</pre>
 *
 * <h3>Usage — UPDATE</h3>
 * <pre>{@code
 * AuditBuilder.forUpdate(entity(), entityId(), changedBy)
 *     .field(Fields.NAME, oldName, newName)
 *     .buildInto(this);
 * }</pre>
 */
public final class AuditBuilder {

    private final String entityType;
    private final String entityId;
    private final AuditAction action;
    private final UserIdentifier changedBy;
    private final List<AuditEntry> entries = new ArrayList<>();

    private AuditBuilder(
            @NonNull String entityType,
            @NonNull String entityId,
            @NonNull AuditAction action,
            @NonNull UserIdentifier changedBy
    ) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.action = action;
        this.changedBy = changedBy;
    }

    public static @NonNull AuditBuilder forCreate(
            @NonNull String entityType,
            @NonNull String entityId,
            @NonNull UserIdentifier changedBy
    ) {
        return new AuditBuilder(entityType, entityId, AuditAction.CREATE, changedBy);
    }

    public static @NonNull AuditBuilder forUpdate(
            @NonNull String entityType,
            @NonNull String entityId,
            @NonNull UserIdentifier changedBy
    ) {
        return new AuditBuilder(entityType, entityId, AuditAction.UPDATE, changedBy);
    }

    public static @NonNull AuditBuilder forDelete(
            @NonNull String entityType,
            @NonNull String entityId,
            @NonNull UserIdentifier changedBy
    ) {
        return new AuditBuilder(entityType, entityId, AuditAction.DELETE, changedBy);
    }

    /**
     * Adds an entry for a single field.
     * Use for CREATE (oldValue = null) and DELETE (newValue = null).
     */
    public @NonNull AuditBuilder field(@NonNull String fieldName, @Nullable String value) {
        this.entries.add(switch (this.action) {
            case CREATE -> AuditEntry.created(this.entityType, this.entityId, fieldName, value, this.changedBy);
            case DELETE -> AuditEntry.deleted(this.entityType, this.entityId, fieldName, value, this.changedBy);
            case UPDATE -> throw new IllegalStateException("Use field(name, old, new) for UPDATE");
        });
        return this;
    }

    /**
     * Adds an entry for an UPDATE — requires both old and new values.
     */
    public @NonNull AuditBuilder field(
            @NonNull String fieldName,
            @Nullable String oldValue,
            @Nullable String newValue
    ) {
        this.entries.add(AuditEntry.updated(this.entityType, this.entityId, fieldName, oldValue, newValue, this.changedBy));
        return this;
    }

    /**
     * Pushes all accumulated entries into the target {@link Auditable}.
     */
    public void buildInto(@NonNull Auditable target) {
        entries.forEach(target::addAuditEntry);
    }
}
