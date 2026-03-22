package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.rule.CounterpartyContactRules;
import com.cloudcontrol.catalog.domain.counterparty.vo.PhoneNumber;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CounterpartyContact implements Entity<Counterparty, CounterpartyContact.CounterpartyContactId> {

    public record CounterpartyContactId(Long value) implements Identifier {
    }

    private final CounterpartyContactId id;
    @Nullable
    private String personName;
    @NonNull
    private PhoneNumber phone;

    static @NonNull CounterpartyContact withPerson(@NonNull String personName, @NonNull PhoneNumber phone) {
        validatePersonName(personName);
        return new CounterpartyContact(null, personName, phone);
    }

    static @NonNull CounterpartyContact phoneOnly(@NonNull PhoneNumber phone) {
        return new CounterpartyContact(null, null, phone);
    }

    void updatePersonName(@Nullable String personName) {
        validatePersonName(personName);
        this.personName = personName;
    }

    void updatePhone(@NonNull PhoneNumber phone) {
        this.phone = phone;
    }

    public boolean hasContactPerson() {
        return personName != null && !personName.isBlank();
    }

    private static void validatePersonName(@Nullable String value) {
        if (value != null && value.length() > CounterpartyContactRules.PersonName.MAX_LENGTH)
            throw new InvalidValueException(CounterpartyContactRules.PersonName.TOO_LONG, CounterpartyContactRules.PersonName.MAX_LENGTH);
    }
}
