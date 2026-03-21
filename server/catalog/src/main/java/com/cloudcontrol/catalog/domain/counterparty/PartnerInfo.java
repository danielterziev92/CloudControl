package com.cloudcontrol.catalog.domain.counterparty;


import com.cloudcontrol.catalog.domain.counterparty.rule.PartnerInfoRules;
import com.cloudcontrol.catalog.domain.counterparty.vo.CompanyRegistrationNumber;
import com.cloudcontrol.catalog.domain.counterparty.vo.TaxNumber;
import com.cloudcontrol.catalog.domain.shared.InvalidValueException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;
import org.jspecify.annotations.NonNull;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PartnerInfo implements Entity<Counterparty, PartnerInfo.PartnerInfoId> {

    public record PartnerInfoId(Long value) implements Identifier {
    }

    private final PartnerInfoId id;
    private String name;
    private String nameEn;
    private final CompanyRegistrationNumber registrationNumber;
    private TaxNumber taxNumber;
    private String custodian;
    private String custodianEn;
    private CounterpartyRelationType type;

    @Contract("_, _, _, _, _, _, _ -> new")
    static @NonNull PartnerInfo create(
            String name,
            String nameEn,
            CompanyRegistrationNumber registrationNumber,
            TaxNumber taxNumber,
            String custodian,
            String custodianEn,
            CounterpartyRelationType type
    ) {
        validateName(name);
        validateNameEn(nameEn);
        validateCustodian(custodian);
        validateCustodianEn(custodianEn);

        return new PartnerInfo(null, name, nameEn, registrationNumber, taxNumber, custodian, custodianEn, type);
    }

    void updateName(String name) {
        validateName(name);
        this.name = name;
    }

    void updateNameEn(String nameEn) {
        validateNameEn(nameEn);
        this.nameEn = nameEn;
    }

    void updateCustodian(String custodian) {
        validateCustodian(custodian);
        this.custodian = custodian;
    }

    void updateCustodianEn(String custodianEn) {
        validateCustodianEn(custodianEn);
        this.custodianEn = custodianEn;
    }

    void assignTaxNumber(TaxNumber taxNumber) {
        this.taxNumber = taxNumber;
    }

    void updateType(CounterpartyRelationType type) {
        this.type = type;
    }

    private static void validateName(String value) {
        if (value == null || value.isBlank())
            throw new InvalidValueException(PartnerInfoRules.Name.BLANK);

        if (value.length() > PartnerInfoRules.Name.MAX_LENGTH)
            throw new InvalidValueException(PartnerInfoRules.Name.TOO_LONG);
    }

    private static void validateNameEn(String value) {
        if (value != null && value.length() > PartnerInfoRules.NameEn.MAX_LENGTH)
            throw new InvalidValueException(PartnerInfoRules.NameEn.TOO_LONG);
    }

    private static void validateCustodian(String value) {
        if (value == null || value.isBlank())
            throw new InvalidValueException(PartnerInfoRules.Custodian.BLANK);

        if (value.length() > PartnerInfoRules.Custodian.MAX_LENGTH)
            throw new InvalidValueException(PartnerInfoRules.Custodian.TOO_LONG);
    }

    private static void validateCustodianEn(String value) {
        if (value != null && value.length() > PartnerInfoRules.CustodianEn.MAX_LENGTH)
            throw new InvalidValueException(PartnerInfoRules.CustodianEn.TOO_LONG);
    }
}
