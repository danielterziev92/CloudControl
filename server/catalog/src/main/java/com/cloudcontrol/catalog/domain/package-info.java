/**
 * Domain layer — pure business logic.
 *
 * <p>Contains aggregates, entities, value objects, domain events, and repository interfaces.
 * Must have <b>zero</b> framework dependencies (no Spring, no JPA, no Jackson).
 *
 * <p>Rules:
 * <ul>
 *   <li>No Spring / Jakarta EE imports</li>
 *   <li>No persistence annotations</li>
 *   <li>May depend only on JDK + jMolecules + jSpecify</li>
 * </ul>
 */
@DomainLayer
package com.cloudcontrol.catalog.domain;

import org.jmolecules.architecture.layered.DomainLayer;
