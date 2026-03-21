package com.cloudcontrol.catalog.domain.counterparty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CounterpartyContact implements Entity<Counterparty, CounterpartyContact.CounterpartyContactId> {

    public record CounterpartyContactId(Long value) implements Identifier {
    }

    private final CounterpartyContactId id;
    private final String personName;
    private final String phone;

    static CounterpartyContact withPerson(String personName, String phone) {
        requirePhone(phone);
        return new CounterpartyContact(null, personName, phone);
    }

    static CounterpartyContact phoneOnly(String phone) {
        requirePhone(phone);
        return new CounterpartyContact(null, null, phone);
    }

    private static void requirePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }
    }

    public boolean hasContactPerson() {
        return personName != null && !personName.isBlank();
    }
}
