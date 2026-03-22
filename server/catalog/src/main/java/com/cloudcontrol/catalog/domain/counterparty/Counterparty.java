package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.rule.CounterpartyRules;
import com.cloudcontrol.catalog.domain.counterparty.vo.*;
import com.cloudcontrol.catalog.domain.shared.BusinessRuleException;
import com.cloudcontrol.catalog.domain.shared.audit.UserIdentifier;
import lombok.AccessLevel;
import lombok.Getter;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Counterparty implements AggregateRoot<Counterparty, Counterparty.CounterpartyId> {

    public record CounterpartyId(Long value) implements Identifier {
    }

    private final CounterpartyId id;
    private final CounterpartyType counterpartyType;
    private boolean active;

    private PartnerInfo partnerInfo;
    private PersonInfo personInfo;

    @Getter(AccessLevel.NONE)
    private final List<CounterpartyAddress> addresses = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    private final List<CounterpartyContact> contacts = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    private final List<CounterpartyEmailAddress> emails = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    private final List<CounterpartyBankAccount> bankAccounts = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    private final List<CounterpartyEvent> domainEvents = new ArrayList<>();

    private Counterparty(CounterpartyId id, CounterpartyType counterpartyType) {
        this.id = id;
        this.counterpartyType = counterpartyType;
        this.active = true;
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

        counterparty.registerEvent(
                new CounterpartyEvent.PartnerRegistered(null, name, registrationNumber.value())
        );

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

        counterparty.registerEvent(
                new CounterpartyEvent.PersonRegistered(null, firstName, lastName)
        );

        return counterparty;
    }

    public void updatePartnerName(@NonNull String name, @NonNull UserIdentifier changedBy) {
        requireOrganization();
        this.partnerInfo.updateName(name, changedBy);

        registerEvent(new CounterpartyEvent.PartnerInfoUpdated(id));
    }

    public void updatePartnerNameEn(@Nullable String nameEn, @NonNull UserIdentifier changedBy) {
        requireOrganization();
        if (nameEn == null) this.partnerInfo.removeNameEn(changedBy);
        else this.partnerInfo.updateNameEn(nameEn, changedBy);

        registerEvent(new CounterpartyEvent.PartnerInfoUpdated(id));
    }

    public void updatePartnerCustodianEn(@Nullable String custodianEn, @NonNull UserIdentifier changedBy) {
        requireOrganization();
        if (custodianEn == null) this.partnerInfo.removeCustodianEn(changedBy);
        else this.partnerInfo.updateCustodianEn(custodianEn, changedBy);

        registerEvent(new CounterpartyEvent.PartnerInfoUpdated(id));
    }

    public void addAddress(
            @NonNull Language language,
            @NonNull CountryCode country,
            @Nullable String region,
            @NonNull String town,
            @NonNull String address,
            boolean isPrimary
    ) {
        if (isPrimary) {
            this.addresses.stream()
                    .filter(a -> a.getLanguage() == language)
                    .forEach(CounterpartyAddress::unmarkAsPrimary);
        }
        this.addresses.add(CounterpartyAddress.create(language, country, region, town, address, isPrimary));

        registerEvent(new CounterpartyEvent.AddressAdded(id, isPrimary));
    }

    public void addContactWithPerson(@NonNull String personName, @NonNull PhoneNumber phone) {
        requireOrganization();
        this.contacts.add(CounterpartyContact.withPerson(personName, phone));

        registerEvent(new CounterpartyEvent.ContactAdded(this.id));
    }

    public void addPhone(@NonNull PhoneNumber phone) {
        this.contacts.add(CounterpartyContact.phoneOnly(phone));

        registerEvent(new CounterpartyEvent.ContactAdded(this.id));
    }

    public void addEmail(@NonNull String email, boolean isPrimary) {
        if (isPrimary) this.emails.forEach(CounterpartyEmailAddress::unmarkAsPrimary);
        this.emails.add(CounterpartyEmailAddress.create(email, isPrimary));

        registerEvent(new CounterpartyEvent.EmailAdded(this.id, isPrimary));
    }

    public void addBankAccount(
            @NonNull String name,
            @Nullable String nameEn,
            @NonNull Bic bic,
            @NonNull Iban iban,
            boolean isPrimary
    ) {
        if (isPrimary) this.bankAccounts.forEach(CounterpartyBankAccount::unmarkAsPrimary);
        this.bankAccounts.add(CounterpartyBankAccount.create(name, nameEn, bic, iban, isPrimary));

        registerEvent(new CounterpartyEvent.BankAccountAdded(this.id, isPrimary));
    }

    public void deactivate() {
        if (!this.active)
            throw new BusinessRuleException(CounterpartyRules.Lifecycle.ALREADY_INACTIVE);

        this.active = false;
        registerEvent(new CounterpartyEvent.CounterpartyDeactivated(id));
    }

    public boolean isOrganization() {
        return counterpartyType == CounterpartyType.ORGANIZATION;
    }

    public boolean isPerson() {
        return counterpartyType == CounterpartyType.PERSON;
    }

    public List<CounterpartyAddress> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }

    public List<CounterpartyContact> getContacts() {
        return Collections.unmodifiableList(contacts);
    }

    public List<CounterpartyEmailAddress> getEmails() {
        return Collections.unmodifiableList(emails);
    }

    public List<CounterpartyBankAccount> getBankAccounts() {
        return Collections.unmodifiableList(bankAccounts);
    }

    public List<CounterpartyEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    private void requireOrganization() {
        if (counterpartyType != CounterpartyType.ORGANIZATION)
            throw new BusinessRuleException(CounterpartyRules.Type.ORGANIZATION_ONLY);
    }

    private void registerEvent(@NonNull CounterpartyEvent event) {
        domainEvents.add(event);
    }
}
