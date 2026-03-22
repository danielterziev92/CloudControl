package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.rule.CounterpartyRules;
import com.cloudcontrol.catalog.domain.counterparty.vo.*;
import com.cloudcontrol.catalog.domain.shared.BusinessRuleException;
import com.cloudcontrol.catalog.domain.shared.audit.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.event.types.DomainEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@FieldNameConstants
public class Counterparty implements AggregateRoot<Counterparty, Counterparty.CounterpartyId>, Auditable {

    public record CounterpartyId(Long value) implements Identifier {
    }

    private final CounterpartyId id;
    private final CounterpartyType counterpartyType;
    private boolean active;

    private PartnerInfo partnerInfo;
    private PersonInfo personInfo;

    @Getter(AccessLevel.NONE)
    @FieldNameConstants.Exclude
    private final List<CounterpartyAddress> addresses;
    @Getter(AccessLevel.NONE)
    @FieldNameConstants.Exclude
    private final List<CounterpartyContact> contacts;
    @Getter(AccessLevel.NONE)
    @FieldNameConstants.Exclude
    private final List<CounterpartyEmailAddress> emails;
    @Getter(AccessLevel.NONE)
    @FieldNameConstants.Exclude
    private final List<CounterpartyBankAccount> bankAccounts;

    @Getter(AccessLevel.NONE)
    @FieldNameConstants.Exclude
    private final List<DomainEvent> domainEvents;

    @Getter(AccessLevel.NONE)
    @FieldNameConstants.Exclude
    private final List<AuditEntry> auditEntries;

    private Counterparty(CounterpartyId id, CounterpartyType counterpartyType) {
        this.id = id;
        this.counterpartyType = counterpartyType;
        this.active = true;

        this.addresses = new ArrayList<>();
        this.contacts = new ArrayList<>();
        this.emails = new ArrayList<>();
        this.bankAccounts = new ArrayList<>();
        this.domainEvents = new ArrayList<>();
        this.auditEntries = new ArrayList<>();
    }

    public static @NonNull Counterparty registerPartner(
            @NonNull String name,
            @Nullable String nameEn,
            @NonNull CompanyRegistrationNumber registrationNumber,
            @Nullable TaxNumber taxNumber,
            @NonNull String custodian,
            @Nullable String custodianEn,
            @NonNull CounterpartyRelationType type,
            @NonNull UserIdentifier changedBy
    ) {
        Counterparty counterparty = new Counterparty(null, CounterpartyType.ORGANIZATION);

        counterparty.partnerInfo = PartnerInfo.create(
                name, nameEn, registrationNumber, taxNumber, custodian, custodianEn, type, changedBy
        );

        counterparty.registerEvent(new CounterpartyEvent.PartnerRegistered(null, name, registrationNumber.value()));
        counterparty.registerAudit(counterparty.partnerInfo);

        return counterparty;
    }

    public static @NonNull Counterparty registerPerson(
            @NonNull String firstName,
            @Nullable String middleName,
            @NonNull String lastName,
            @NonNull PersonalIdentificationNumber pin,
            @NonNull CounterpartyRelationType type,
            @NonNull UserIdentifier changedBy
    ) {
        Counterparty counterparty = new Counterparty(null, CounterpartyType.PERSON);

        counterparty.personInfo = PersonInfo.create(firstName, middleName, lastName, pin, type, changedBy);

        counterparty.registerEvent(new CounterpartyEvent.PersonRegistered(null, firstName, lastName));
        counterparty.registerAudit(counterparty.personInfo);

        return counterparty;
    }

    public void updatePartnerName(@NonNull String name, @NonNull UserIdentifier changedBy) {
        requireOrganization();
        this.partnerInfo.updateName(name, changedBy);

        registerEvent(new CounterpartyEvent.PartnerInfoUpdated(this.id));
        registerAudit(this.partnerInfo);
    }

    public void updatePartnerNameEn(@Nullable String nameEn, @NonNull UserIdentifier changedBy) {
        requireOrganization();
        if (nameEn == null) this.partnerInfo.removeNameEn(changedBy);
        else this.partnerInfo.updateNameEn(nameEn, changedBy);

        registerEvent(new CounterpartyEvent.PartnerInfoUpdated(this.id));
        registerAudit(this.partnerInfo);
    }

    public void updatePartnerCustodian(@NonNull String custodian, @NonNull UserIdentifier changedBy) {
        requireOrganization();
        this.partnerInfo.updateCustodian(custodian, changedBy);

        registerEvent(new CounterpartyEvent.PartnerInfoUpdated(this.id));
        registerAudit(this.partnerInfo);
    }

    public void updatePartnerCustodianEn(@Nullable String custodianEn, @NonNull UserIdentifier changedBy) {
        requireOrganization();
        if (custodianEn == null) this.partnerInfo.removeCustodianEn(changedBy);
        else this.partnerInfo.updateCustodianEn(custodianEn, changedBy);

        registerEvent(new CounterpartyEvent.PartnerInfoUpdated(this.id));
        registerAudit(this.partnerInfo);
    }

    public void addAddress(
            @NonNull Language language,
            @NonNull CountryCode country,
            @Nullable String region,
            @NonNull String town,
            @NonNull String address,
            boolean isPrimary,
            @NonNull UserIdentifier changedBy
    ) {
        if (isPrimary)
            this.addresses.stream()
                    .filter(a -> a.getLanguage() == language)
                    .forEach(a -> a.unmarkAsPrimary(changedBy));

        CounterpartyAddress counterpartyAddress = CounterpartyAddress.create(
                language, country, region, town, address, isPrimary, changedBy
        );
        this.addresses.add(counterpartyAddress);

        registerEvent(new CounterpartyEvent.AddressAdded(this.id, isPrimary));
        registerAudit(counterpartyAddress);
    }

    public void addContactWithPerson(
            @NonNull String personName,
            @NonNull PhoneNumber phone,
            @NonNull UserIdentifier changedBy
    ) {
        requireOrganization();
        CounterpartyContact contact = CounterpartyContact.withPerson(personName, phone, changedBy);
        contacts.add(contact);

        registerEvent(new CounterpartyEvent.ContactAdded(this.id));
        registerAudit(contact);
    }

    public void addPhone(@NonNull PhoneNumber phone, @NonNull UserIdentifier changedBy) {
        CounterpartyContact contact = CounterpartyContact.phoneOnly(phone, changedBy);
        this.contacts.add(contact);

        registerEvent(new CounterpartyEvent.ContactAdded(this.id));
        registerAudit(contact);
    }

    public void addEmail(@NonNull String email, boolean isPrimary, @NonNull UserIdentifier changedBy) {
        if (isPrimary) this.emails.forEach(e -> e.unmarkAsPrimary(changedBy));
        CounterpartyEmailAddress emailAddr = CounterpartyEmailAddress.create(email, isPrimary, changedBy);
        this.emails.add(emailAddr);

        registerEvent(new CounterpartyEvent.EmailAdded(this.id, isPrimary));
        registerAudit(emailAddr);
    }

    public void addBankAccount(
            @NonNull String name,
            @Nullable String nameEn,
            @NonNull Bic bic,
            @NonNull Iban iban,
            boolean isPrimary,
            @NonNull UserIdentifier changedBy
    ) {
        if (isPrimary) this.bankAccounts.forEach(b -> b.unmarkAsPrimary(changedBy));
        CounterpartyBankAccount account = CounterpartyBankAccount.create(name, nameEn, bic, iban, isPrimary, changedBy);
        this.bankAccounts.add(account);

        registerEvent(new CounterpartyEvent.BankAccountAdded(this.id, isPrimary));
        registerAudit(account);
    }

    public void deactivate(@NonNull UserIdentifier changedBy) {
        if (!this.active)
            throw new BusinessRuleException(CounterpartyRules.Lifecycle.ALREADY_INACTIVE);

        AuditBuilder.forUpdate(Counterparty.class.getSimpleName(), entityId(), changedBy)
                .field(Fields.active, "true", "false")
                .buildInto(this);
        this.active = false;

        registerEvent(new CounterpartyEvent.CounterpartyDeactivated(id));
        registerAudit(this);
    }

    public boolean isOrganization() {
        return this.counterpartyType == CounterpartyType.ORGANIZATION;
    }

    public boolean isPerson() {
        return this.counterpartyType == CounterpartyType.PERSON;
    }

    public List<CounterpartyAddress> getAddresses() {
        return Collections.unmodifiableList(this.addresses);
    }

    public List<CounterpartyContact> getContacts() {
        return Collections.unmodifiableList(this.contacts);
    }

    public List<CounterpartyEmailAddress> getEmails() {
        return Collections.unmodifiableList(this.emails);
    }

    public List<CounterpartyBankAccount> getBankAccounts() {
        return Collections.unmodifiableList(this.bankAccounts);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
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

    private void requireOrganization() {
        if (this.counterpartyType != CounterpartyType.ORGANIZATION)
            throw new BusinessRuleException(CounterpartyRules.Type.ORGANIZATION_ONLY);
    }

    private void registerEvent(@NonNull DomainEvent event) {
        this.domainEvents.add(event);
    }

    private void registerAudit(@NonNull Auditable entity) {
        List<AuditEntry> entries = List.copyOf(entity.getAuditEntries());
        if (entries.isEmpty()) return;

        registerEvent(new AuditEvent(List.copyOf(entity.getAuditEntries())));
        entity.clearAuditEntries();
    }

    private @NonNull String entityId() {
        return this.id != null ? this.id.value().toString() : "new";
    }
}
