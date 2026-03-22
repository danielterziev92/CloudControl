package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.rule.CounterpartyAddressRules;
import com.cloudcontrol.catalog.domain.counterparty.vo.CountryCode;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CounterpartyAddress implements Entity<Counterparty, CounterpartyAddress.CounterpartyAddressId> {
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

    static @NonNull CounterpartyAddress create(
            @NonNull Language language,
            @NonNull CountryCode country,
            @Nullable String region,
            @NonNull String town,
            @NonNull String address,
            boolean isPrimary
    ) {
        validateRegion(region);
        validateTown(town);
        validateAddress(address);

        return new CounterpartyAddress(null, language, country, region, town, address, isPrimary);
    }

    void updateCountry(@NonNull CountryCode country) {
        this.country = country;
    }

    void updateRegion(@Nullable String region) {
        validateRegion(region);
        this.region = region;
    }

    void updateTown(@NonNull String town) {
        validateTown(town);
        this.town = town;
    }

    void updateAddress(@NonNull String address) {
        validateAddress(address);
        this.address = address;
    }

    void markAsPrimary() {
        this.isPrimary = true;
    }

    void unmarkAsPrimary() {
        this.isPrimary = false;
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
}
