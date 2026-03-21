package com.cloudcontrol.catalog.domain.counterparty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PersonInfo implements Entity<Counterparty, PersonInfo.PersonInfoId> {
    public record PersonInfoId(Long value) implements Identifier {
    }

    private final PersonInfoId id;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String pin;
    private final String type;

    static PersonInfo create(
            String firstName,
            String middleName,
            String lastName,
            String pin,
            String type
    ) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Person type is required");
        }
        return new PersonInfo(null, firstName, middleName, lastName, pin, type);
    }

    public String fullName() {
        if (middleName != null && !middleName.isBlank()) {
            return firstName + " " + middleName + " " + lastName;
        }
        return firstName + " " + lastName;
    }

}
