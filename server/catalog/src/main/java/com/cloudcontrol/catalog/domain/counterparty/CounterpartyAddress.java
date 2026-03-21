package com.cloudcontrol.catalog.domain.counterparty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CounterpartyAddress implements Entity<Counterparty, CounterpartyAddress.CounterpartyAddressId> {
    public record CounterpartyAddressId(Long value) implements Identifier {
    }

    public enum Language {
        BG, EN
    }

    private final CounterpartyAddressId id;
    private final Language language;
    private final String country;
    private final String region;
    private final String town;
    private final String address;
    private boolean isPrimary;

    static CounterpartyAddress create(
            Language language,
            String country,
            String region,
            String town,
            String address,
            boolean isPrimary
    ) {
        if (language == null) {
            throw new IllegalArgumentException("Language is required");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country is required");
        }
        if (town == null || town.isBlank()) {
            throw new IllegalArgumentException("Town is required");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }
        return new CounterpartyAddress(null, language, country, region, town, address, isPrimary);
    }

    void markAsPrimary() {
        this.isPrimary = true;
    }

    void unmarkAsPrimary() {
        this.isPrimary = false;
    }
}
