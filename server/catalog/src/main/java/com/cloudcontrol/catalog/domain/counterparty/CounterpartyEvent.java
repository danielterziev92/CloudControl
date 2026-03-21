package com.cloudcontrol.catalog.domain.counterparty;

import org.jmolecules.event.types.DomainEvent;

public sealed interface CounterpartyEvent extends DomainEvent permits
        CounterpartyEvent.PartnerRegistered,
        CounterpartyEvent.PersonRegistered,
        CounterpartyEvent.PartnerInfoUpdated,
        CounterpartyEvent.CounterpartyDeactivated,
        CounterpartyEvent.AddressAdded,
        CounterpartyEvent.ContactAdded,
        CounterpartyEvent.EmailAdded,
        CounterpartyEvent.BankAccountAdded {

    Counterparty.CounterpartyId counterpartyId();

    record PartnerRegistered(
            Counterparty.CounterpartyId counterpartyId,
            String nameBg,
            String uic
    ) implements CounterpartyEvent {
    }

    record PersonRegistered(
            Counterparty.CounterpartyId counterpartyId,
            String firstName,
            String lastName
    ) implements CounterpartyEvent {
    }

    record PartnerInfoUpdated(
            Counterparty.CounterpartyId counterpartyId
    ) implements CounterpartyEvent {
    }

    record CounterpartyDeactivated(
            Counterparty.CounterpartyId counterpartyId
    ) implements CounterpartyEvent {
    }

    record AddressAdded(
            Counterparty.CounterpartyId counterpartyId,
            boolean isPrimary
    ) implements CounterpartyEvent {
    }

    record ContactAdded(
            Counterparty.CounterpartyId counterpartyId
    ) implements CounterpartyEvent {
    }

    record EmailAdded(
            Counterparty.CounterpartyId counterpartyId,
            boolean isPrimary
    ) implements CounterpartyEvent {
    }

    record BankAccountAdded(
            Counterparty.CounterpartyId counterpartyId,
            boolean isPrimary
    ) implements CounterpartyEvent {
    }
}
