package com.cloudcontrol.catalog.domain.counterparty;

import com.cloudcontrol.catalog.domain.counterparty.vo.Iban;
import com.cloudcontrol.catalog.domain.counterparty.vo.Uic;
import com.cloudcontrol.catalog.domain.counterparty.vo.Vat;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Counterparty implements AggregateRoot<Counterparty, Counterparty.CounterpartyId> {

    public record CounterpartyId(Long value) implements Identifier {
    }

    private final CounterpartyId id;
    private final CounterpartyType counterpartyType;
    private boolean active;

    private PartnerInfo partnerInfo;
    private PersonInfo personInfo;

    private final List<CounterpartyAddress> addresses = new ArrayList<>();
    private final List<CounterpartyContact> contacts = new ArrayList<>();
    private final List<CounterpartyEmailAddress> emails = new ArrayList<>();
    private final List<CounterpartyBankAccount> bankAccounts = new ArrayList<>();

    private final List<CounterpartyEvent> domainEvents = new ArrayList<>();

    private Counterparty(CounterpartyId id, CounterpartyType counterpartyType) {
        this.id = id;
        this.counterpartyType = counterpartyType;
        this.active = true;
    }

    public static Counterparty registerPartner(
            String nameBg,
            String nameEn,
            Uic uic,
            Vat vat,
            String custodian,
            String custodianEn,
            String type
    ) {
        Counterparty counterparty = new Counterparty(null, CounterpartyType.ORGANIZATION);
        counterparty.partnerInfo = PartnerInfo.create(nameBg, nameEn, uic, vat, custodian, custodianEn, type);
        counterparty.registerEvent(
                new CounterpartyEvent.PartnerRegistered(null, nameBg, uic.value())
        );
        return counterparty;
    }

    public static Counterparty registerPerson(
            String firstName,
            String middleName,
            String lastName,
            String pin,
            String type
    ) {
        Counterparty counterparty = new Counterparty(null, CounterpartyType.PERSON);
        counterparty.personInfo = PersonInfo.create(firstName, middleName, lastName, pin, type);
        counterparty.registerEvent(
                new CounterpartyEvent.PersonRegistered(null, firstName, lastName)
        );
        return counterparty;
    }

    public void updatePartnerNames(String nameBg, String nameEn) {
        requireOrganization("Cannot update partner names on a Person counterparty");
        partnerInfo.updateNames(nameBg, nameEn);
        registerEvent(new CounterpartyEvent.PartnerInfoUpdated(id));
    }

    public void updatePartnerCustodian(String custodian, String custodianEn) {
        requireOrganization("Cannot update custodian on a Person counterparty");
        partnerInfo.updateCustodian(custodian, custodianEn);
        registerEvent(new CounterpartyEvent.PartnerInfoUpdated(id));
    }

    public void assignVat(Vat vat) {
        requireOrganization("VAT can only be assigned to organizations");
        partnerInfo.assignVat(vat);
        registerEvent(new CounterpartyEvent.PartnerInfoUpdated(id));
    }

    public void addAddress(
            Language language,
            String country,
            String region,
            String town,
            String address,
            boolean isPrimary
    ) {
        if (isPrimary) {
            addresses.stream()
                    .filter(a -> a.getLanguage() == language)
                    .forEach(CounterpartyAddress::unmarkAsPrimary);
        }
        CounterpartyAddress newAddress = CounterpartyAddress.create(
                language, country, region, town, address, isPrimary
        );
        addresses.add(newAddress);
        registerEvent(new CounterpartyEvent.AddressAdded(id, isPrimary));
    }

    public void addContactWithPerson(String personName, String phone) {
        requireOrganization("Contacts with person name are only for organizations");
        contacts.add(CounterpartyContact.withPerson(personName, phone));
        registerEvent(new CounterpartyEvent.ContactAdded(id));
    }

    public void addPhone(String phone) {
        contacts.add(CounterpartyContact.phoneOnly(phone));
        registerEvent(new CounterpartyEvent.ContactAdded(id));
    }

    public void addEmail(String email, boolean isPrimary) {
        if (isPrimary) {
            emails.forEach(CounterpartyEmailAddress::unmarkAsPrimary);
        }
        emails.add(CounterpartyEmailAddress.create(email, isPrimary));
        registerEvent(new CounterpartyEvent.EmailAdded(id, isPrimary));
    }

    public void addBankAccount(String name, String bic, Iban iban, boolean isPrimary) {
        if (isPrimary) {
            bankAccounts.forEach(CounterpartyBankAccount::unmarkAsPrimary);
        }
        bankAccounts.add(CounterpartyBankAccount.create(name, bic, iban, isPrimary));
        registerEvent(new CounterpartyEvent.BankAccountAdded(id, isPrimary));
    }

    public void deactivate() {
        if (!this.active) {
            throw new IllegalStateException("Counterparty is already inactive");
        }
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

    private void requireOrganization(String message) {
        if (counterpartyType != CounterpartyType.ORGANIZATION) {
            throw new IllegalStateException(message);
        }
    }

    private void registerEvent(CounterpartyEvent event) {
        domainEvents.add(event);
    }
}
