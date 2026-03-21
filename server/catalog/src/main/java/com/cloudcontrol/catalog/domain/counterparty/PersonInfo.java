package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.rule.PersonInfoRules;
import com.cloudcontrol.catalog.domain.counterparty.vo.PersonalIdentificationNumber;
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
public class PersonInfo implements Entity<Counterparty, PersonInfo.PersonInfoId> {
    public record PersonInfoId(Long value) implements Identifier {
    }

    private final PersonInfoId id;
    @NonNull
    private String firstName;
    @Nullable
    private String middleName;
    @NonNull
    private String lastName;
    @NonNull
    private PersonalIdentificationNumber pin;
    @NonNull
    private CounterpartyRelationType type;

    static @NonNull PersonInfo create(
            @NonNull String firstName,
            @Nullable String middleName,
            @NonNull String lastName,
            @NonNull PersonalIdentificationNumber pin,
            @NonNull CounterpartyRelationType type
    ) {
        validateFirstName(firstName);
        validateMiddleName(middleName);
        validateLastName(lastName);

        return new PersonInfo(null, firstName, middleName, lastName, pin, type);
    }

    void updateFirstName(@NonNull String firstName) {
        validateFirstName(firstName);
        this.firstName = firstName;
    }

    void updateMiddleName(@NonNull String middleName) {
        validateMiddleName(middleName);
        this.middleName = middleName;
    }

    void removeMiddleName() {
        this.middleName = null;
    }

    void updateLastName(@NonNull String lastName) {
        validateLastName(lastName);
        this.lastName = lastName;
    }

    void updatePin(@NonNull PersonalIdentificationNumber pin) {
        this.pin = pin;
    }

    void updateType(@NonNull CounterpartyRelationType type) {
        this.type = type;
    }

    public @NonNull String fullName() {
        if (middleName != null && !middleName.isBlank())
            return String.format("%s %s %s", firstName, middleName, lastName);
        return String.format("%s %s", firstName, lastName);
    }

    private static void validateFirstName(@NonNull String value) {
        if (value.isBlank())
            throw new InvalidValueException(PersonInfoRules.FirstName.BLANK);

        if (value.length() > PersonInfoRules.FirstName.MAX_LENGTH)
            throw new InvalidValueException(PersonInfoRules.FirstName.TOO_LONG, PersonInfoRules.FirstName.MAX_LENGTH);
    }

    private static void validateMiddleName(@Nullable String value) {
        if (value != null && value.length() > PersonInfoRules.MiddleName.MAX_LENGTH)
            throw new InvalidValueException(PersonInfoRules.MiddleName.TOO_LONG, PersonInfoRules.MiddleName.MAX_LENGTH);
    }

    private static void validateLastName(@NonNull String value) {
        if (value.isBlank())
            throw new InvalidValueException(PersonInfoRules.LastName.BLANK);

        if (value.length() > PersonInfoRules.LastName.MAX_LENGTH)
            throw new InvalidValueException(PersonInfoRules.LastName.TOO_LONG, PersonInfoRules.LastName.MAX_LENGTH);
    }
}
