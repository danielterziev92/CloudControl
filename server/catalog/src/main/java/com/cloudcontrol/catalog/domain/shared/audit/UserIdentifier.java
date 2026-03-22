package com.cloudcontrol.catalog.domain.shared.audit;

import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import org.jmolecules.ddd.types.ValueObject;
import org.jspecify.annotations.NonNull;

/**
 * Identifies the user who performed an audited action.
 *
 * <p>Currently a plain string (email, username, or system identifier).
 * When a {@code User} aggregate is introduced, call sites can migrate to
 * {@code UserIdentifier.of(user.getEmail())} without touching the audit infrastructure.</p>
 *
 * <p>Special value {@link #SYSTEM} is provided for automated/background operations.</p>
 */
public record UserIdentifier(String value) implements ValueObject {

    /**
     * Sentinel for system-initiated changes (migrations, batch jobs, etc.).
     */
    public static final UserIdentifier SYSTEM = new UserIdentifier("system");

    public UserIdentifier {
        if (value == null || value.isBlank()) throw new InvalidValueException("user_identifier.blank");
    }

    public static @NonNull UserIdentifier of(String value) {
        return new UserIdentifier(value);
    }
}
