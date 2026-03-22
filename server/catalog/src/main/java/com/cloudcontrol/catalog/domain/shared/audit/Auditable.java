package com.cloudcontrol.catalog.domain.shared.audit;

import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Marks a domain entity as auditable.
 *
 * <p>Implementations collect {@link AuditEntry} records during mutations
 * and expose them for the application layer to persist.</p>
 */
public interface Auditable {

    /**
     * Returns all audit entries accumulated since the last {@link #clearAuditEntries()} call.
     * The returned list is unmodifiable.
     */
    @NonNull List<AuditEntry> getAuditEntries();

    /**
     * Adds a single audit entry — called by {@link AuditBuilder}.
     */
    void addAuditEntry(@NonNull AuditEntry entry);

    /**
     * Clears the accumulated audit entries — called after they have been persisted.
     */
    void clearAuditEntries();
}
