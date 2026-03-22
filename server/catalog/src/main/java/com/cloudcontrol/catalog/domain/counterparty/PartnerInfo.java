package com.cloudcontrol.catalog.domain.counterparty;


import com.cloudcontrol.catalog.domain.counterparty.rule.PartnerInfoRules;
import com.cloudcontrol.catalog.domain.counterparty.vo.CompanyRegistrationNumber;
import com.cloudcontrol.catalog.domain.counterparty.vo.TaxNumber;
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
public class PartnerInfo implements Entity<Counterparty, PartnerInfo.PartnerInfoId>, Auditable {

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

    @Getter(AccessLevel.NONE)
    @FieldNameConstants.Exclude
    private final List<AuditEntry> auditEntries;

    static @NonNull PartnerInfo create(
            @NonNull String name,
            @Nullable String nameEn,
            @NonNull CompanyRegistrationNumber registrationNumber,
            @Nullable TaxNumber taxNumber,
            @NonNull String custodian,
            @Nullable String custodianEn,
            @NonNull CounterpartyRelationType type,
            @NonNull UserIdentifier changedBy
    ) {
        validateName(name);
        validateNameEn(nameEn);
        validateCustodian(custodian);
        validateCustodianEn(custodianEn);

        PartnerInfo partnerInfo = new PartnerInfo(
                null, name, nameEn, registrationNumber,
                taxNumber, custodian, custodianEn, type, new ArrayList<>()
        );

        AuditBuilder.forCreate(entity(), "new", changedBy)
                .field(Fields.name, name)
                .field(Fields.nameEn, nameEn)
                .field(Fields.registrationNumber, registrationNumber.value())
                .field(Fields.taxNumber, taxNumber != null ? taxNumber.value() : null)
                .field(Fields.custodian, custodian)
                .field(Fields.custodianEn, custodianEn)
                .field(Fields.type, type.name())
                .buildInto(partnerInfo);

        return partnerInfo;
    }

    void updateName(@NonNull String name, @NonNull UserIdentifier changedBy) {
        validateName(name);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.name, this.name, name)
                .buildInto(this);

        this.name = name;
    }

    void updateNameEn(@NonNull String name, @NonNull UserIdentifier changedBy) {
        validateNameEn(nameEn);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.nameEn, this.nameEn, nameEn)
                .buildInto(this);

        this.nameEn = nameEn;
    }

    void removeNameEn(@NonNull UserIdentifier changedBy) {
        AuditBuilder.forDelete(entity(), entityId(), changedBy)
                .field(Fields.nameEn, this.nameEn)
                .buildInto(this);

        this.nameEn = null;
    }

    void updateCustodian(@NonNull String custodian, @NonNull UserIdentifier changedBy) {
        validateCustodian(custodian);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.custodian, this.custodian, custodian)
                .buildInto(this);

        this.custodian = custodian;
    }

    void updateCustodianEn(@NonNull String custodianEn, @NonNull UserIdentifier changedBy) {
        validateCustodianEn(custodianEn);
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.custodianEn, this.custodianEn, custodianEn)
                .buildInto(this);

        this.custodianEn = custodianEn;
    }

    void removeCustodianEn(@NonNull UserIdentifier changedBy) {
        AuditBuilder.forDelete(entity(), entityId(), changedBy)
                .field(Fields.custodianEn, this.custodianEn)
                .buildInto(this);

        this.custodianEn = null;
    }

    void assignTaxNumber(@NonNull TaxNumber taxNumber, @NonNull UserIdentifier changedBy) {
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.taxNumber,
                        this.taxNumber != null ? this.taxNumber.value() : null,
                        taxNumber.value())
                .buildInto(this);

        this.taxNumber = taxNumber;
    }

    void updateType(@NonNull CounterpartyRelationType type, @NonNull UserIdentifier changedBy) {
        AuditBuilder.forUpdate(entity(), entityId(), changedBy)
                .field(Fields.type, this.type.name(), type.name())
                .buildInto(this);

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

    private static @NonNull String entity() {
        return PartnerInfo.class.getSimpleName();
    }

    private @NonNull String entityId() {
        return id != null ? id.value().toString() : "new";
    }
}
