package com.vicchern.deliveryservice.repositories;

import com.vicchern.deliveryservice.models.impl.ActorRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for work with Actor role
 */
public interface ActorRoleRepository extends JpaRepository<ActorRole, Long> {

    /**
     * Retrieves an Actor role by its name.
     *
     * @param name
     * @return the Actor role with the given name
     */
    Optional<ActorRole> findByName(String name);
}
