package com.cloudcontrol.catalog.domain.counterparty;


import com.cloudcontrol.catalog.domain.counterparty.rule.PartnerInfoRules;
import com.cloudcontrol.catalog.domain.counterparty.vo.CompanyRegistrationNumber;
import com.cloudcontrol.catalog.domain.counterparty.vo.TaxNumber;
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
public class PartnerInfo implements Entity<Counterparty, PartnerInfo.PartnerInfoId> {

    public record PartnerInfoId(Long value) implements Identifier {
    }

    private final PartnerInfoId id;
    @NonNull
    private String name;
    @Nullable
    private String nameEn;
    @NonNull
    private final CompanyRegistrationNumber registrationNumber;
    @Nullable
    private TaxNumber taxNumber;
    @NonNull
    private String custodian;
    @Nullable
    private String custodianEn;
    @NonNull
    private CounterpartyRelationType type;

    static @NonNull PartnerInfo create(
            @NonNull String name,
            @Nullable String nameEn,
            @NonNull CompanyRegistrationNumber registrationNumber,
            @Nullable TaxNumber taxNumber,
            @NonNull String custodian,
            @Nullable String custodianEn,
            @NonNull CounterpartyRelationType type
    ) {
        validateName(name);
        validateNameEn(nameEn);
        validateCustodian(custodian);
        validateCustodianEn(custodianEn);

        return new PartnerInfo(null, name, nameEn, registrationNumber, taxNumber, custodian, custodianEn, type);
    }

    void updateName(@NonNull String name) {
        validateName(name);
        this.name = name;
    }

    void updateNameEn(@NonNull String nameEn) {
        validateNameEn(nameEn);
        this.nameEn = nameEn;
    }

    void removeNameEn() {
        this.nameEn = null;
    }

    void updateCustodian(@NonNull String custodian) {
        validateCustodian(custodian);
        this.custodian = custodian;
    }

    void updateCustodianEn(@NonNull String custodianEn) {
        validateCustodianEn(custodianEn);
        this.custodianEn = custodianEn;
    }

    void removeCustodianEn() {
        this.custodianEn = null;
    }

    void assignTaxNumber(@NonNull TaxNumber taxNumber) {
        this.taxNumber = taxNumber;
    }

    void updateType(@NonNull CounterpartyRelationType type) {
        this.type = type;
    }

    private static void validateName(@NonNull String value) {
        if (value.isBlank())
            throw new InvalidValueException(PartnerInfoRules.Name.BLANK);

        if (value.length() > PartnerInfoRules.Name.MAX_LENGTH)
            throw new InvalidValueException(PartnerInfoRules.Name.TOO_LONG);
    }

    private static void validateNameEn(@Nullable String value) {
        if (value != null && value.length() > PartnerInfoRules.NameEn.MAX_LENGTH)
            throw new InvalidValueException(PartnerInfoRules.NameEn.TOO_LONG);
    }

    private static void validateCustodian(@NonNull String value) {
        if (value.isBlank())
            throw new InvalidValueException(PartnerInfoRules.Custodian.BLANK);

        if (value.length() > PartnerInfoRules.Custodian.MAX_LENGTH)
            throw new InvalidValueException(PartnerInfoRules.Custodian.TOO_LONG);
    }

    private static void validateCustodianEn(@Nullable String value) {
        if (value != null && value.length() > PartnerInfoRules.CustodianEn.MAX_LENGTH)
            throw new InvalidValueException(PartnerInfoRules.CustodianEn.TOO_LONG);
    }
}
