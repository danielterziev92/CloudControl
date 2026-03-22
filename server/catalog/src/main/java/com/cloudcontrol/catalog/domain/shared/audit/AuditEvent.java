package com.cloudcontrol.catalog.domain.shared.audit;

import org.jmolecules.event.types.DomainEvent;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Domain event carrying field-level audit entries produced by a single operation.
 * Fired by any aggregate after a mutation — infrastructure listeners persist the entries.
 *
 * <p>Generic — not bound to any specific aggregate or bounded context.</p>
 */
public record AuditEvent(@NonNull List<AuditEntry> entries) implements DomainEvent {
}
