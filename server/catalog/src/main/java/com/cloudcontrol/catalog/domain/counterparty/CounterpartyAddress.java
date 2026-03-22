package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.rule.CounterpartyAddressRules;
import com.cloudcontrol.catalog.domain.counterparty.vo.CountryCode;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import com.cloudcontrol.catalog.domain.shared.audit.AuditBuilder;
import com.cloudcontrol.catalog.domain.shared.audit.AuditEntry;
import com.cloudcontrol.catalog.domain.shared.audit.Auditable;
import com.cloudcontrol.catalog.domain.shared.audit.UserIdentifier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@FieldNameConstants
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CounterpartyAddress implements Entity<Counterparty, CounterpartyAddress.CounterpartyAddressId>, Auditable {
    public record CounterpartyAddressId(Long value) implements Identifier {
    }

    private final CounterpartyAddressId id;
    @NonNull
    private final Language language;
    @NonNull
    private CountryCode country;
    @Nullable
    private String region;
    @NonNull
    private String town;
    @NonNull
    private String address;
    private boolean isPrimary;

    @Getter(AccessLevel.NONE)
    @FieldNameConstants.Exclude
    private final List<AuditEntry> auditEntries;

    static @NonNull CounterpartyAddress create(
            @NonNull Language language,
            @NonNull CountryCode country,
            @Nullable String region,
            @NonNull String town,
            @NonNull String address,
            boolean isPrimary,
            @NonNull UserIdentifier changedBy
    ) {
        validateRegion(region);
        validateTown(town);
        validateAddress(address);

        CounterpartyAddress counterpartyAddress = new CounterpartyAddress(
                null, language, country, region, town, address, isPrimary, new ArrayList<>()
        );

        AuditBuilder.forCreate(entity(), counterpartyAddress.entityId(), changedBy)
                .field(Fields.address, address)
                .field(Fields.country, country.value())
                .field(Fields.region, region)
                .field(Fields.town, town)
                .field(Fields.address, address)
                .field(Fields.isPrimary, isPrimary ? "true" : "false")
                .buildInto(counterpartyAddress);

        return counterpartyAddress;
    }

    void updateCountry(@NonNull CountryCode country, @NonNull UserIdentifier changedBy) {
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.country, this.country.value(), country.value())
                .buildInto(this);

        this.country = country;
    }

    void updateRegion(@Nullable String region, @NonNull UserIdentifier changedBy) {
        validateRegion(region);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.region, this.region, region)
                .buildInto(this);

        this.region = region;
    }

    void updateTown(@NonNull String town, @NonNull UserIdentifier changedBy) {
        validateTown(town);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.town, this.town, town)
                .buildInto(this);

        this.town = town;
    }

    void updateAddress(@NonNull String address, @NonNull UserIdentifier changedBy) {
        validateAddress(address);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.address, this.address, address)
                .buildInto(this);

        this.address = address;
    }

    void markAsPrimary(@NonNull UserIdentifier changedBy) {
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.isPrimary, this.isPrimary ? "true" : "false", "true")
                .buildInto(this);

        this.isPrimary = true;
    }

    void unmarkAsPrimary(@NonNull UserIdentifier changedBy) {
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.isPrimary, this.isPrimary ? "true" : "false", "false")
                .buildInto(this);

        this.isPrimary = false;
    }

    @Override
    public @NonNull List<AuditEntry> getAuditEntries() {
        return Collections.unmodifiableList(this.auditEntries);
    }

    @Override
    public void addAuditEntry(@NonNull AuditEntry entry) {
        this.auditEntries.add(entry);
    }

    @Override
    public void clearAuditEntries() {
        this.auditEntries.clear();
    }

    private static void validateRegion(@Nullable String value) {
        if (value != null && value.length() > CounterpartyAddressRules.Region.MAX_LENGTH)
            throw new InvalidValueException(CounterpartyAddressRules.Region.TOO_LONG, CounterpartyAddressRules.Region.MAX_LENGTH);
    }

    private static void validateTown(@NonNull String value) {
        if (value.isBlank())
            throw new InvalidValueException(CounterpartyAddressRules.Town.BLANK);

        if (value.length() > CounterpartyAddressRules.Town.MAX_LENGTH)
            throw new InvalidValueException(CounterpartyAddressRules.Town.TOO_LONG, CounterpartyAddressRules.Town.MAX_LENGTH);
    }

    private static void validateAddress(@NonNull String value) {
        if (value.isBlank())
            throw new InvalidValueException(CounterpartyAddressRules.Address.BLANK);

        if (value.length() > CounterpartyAddressRules.Address.MAX_LENGTH)
            throw new InvalidValueException(CounterpartyAddressRules.Address.TOO_LONG, CounterpartyAddressRules.Address.MAX_LENGTH);
    }

    private static @NonNull String entity() {
        return CounterpartyAddress.class.getSimpleName();
    }

    private @NonNull String entityId() {
        return this.id != null ? this.id.value().toString() : "new";
    }
}
