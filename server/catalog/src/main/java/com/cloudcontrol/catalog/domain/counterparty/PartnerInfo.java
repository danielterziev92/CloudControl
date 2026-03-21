package com.cloudcontrol.catalog.domain.counterparty;


import com.cloudcontrol.catalog.domain.counterparty.vo.Uic;
import com.cloudcontrol.catalog.domain.counterparty.vo.Vat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PartnerInfo implements Entity<Counterparty, PartnerInfo.PartnerInfoId> {

    public record PartnerInfoId(Long value) implements Identifier {
    }

    private final PartnerInfoId id;
    private String nameBg;
    private String nameEn;
    private final Uic uic;
    private Vat vat;
    private String custodian;
    private String custodianEn;
    private String type;

    static PartnerInfo create(
            String nameBg,
            String nameEn,
            Uic uic,
            Vat vat,
            String custodian,
            String custodianEn,
            String type
    ) {
        if (nameBg == null || nameBg.isBlank()) {
            throw new IllegalArgumentException("Partner BG name is required");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Partner type is required");
        }
        return new PartnerInfo(null, nameBg, nameEn, uic, vat, custodian, custodianEn, type);
    }

    void updateNames(String nameBg, String nameEn) {
        if (nameBg == null || nameBg.isBlank()) {
            throw new IllegalArgumentException("Partner BG name cannot be blank");
        }
        this.nameBg = nameBg;
        this.nameEn = nameEn;
    }

    void updateCustodian(String custodian, String custodianEn) {
        this.custodian = custodian;
        this.custodianEn = custodianEn;
    }

    void assignVat(Vat vat) {
        this.vat = vat;
    }
}
