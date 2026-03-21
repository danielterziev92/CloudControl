package com.cloudcontrol.catalog.domain.counterparty;


import org.jmolecules.ddd.types.Repository;

import java.util.Optional;

public interface CounterpartyRepository extends Repository<Counterparty, Counterparty.CounterpartyId> {

    Counterparty save(Counterparty counterparty);

    Optional<Counterparty> findById(Counterparty.CounterpartyId id);

    boolean existsByUic(String uic);
}
