package com.vicchern.deliveryservice.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.vicchern.deliveryservice.models.IUser;
import com.vicchern.deliveryservice.models.impl.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for work with Actor role
 */
public interface ActorRepository extends JpaRepository<Actor, Long> {
    /**
     * Retrieves an Actor  by its guid.
     *
     * @param guid
     * @return the Actor  with the given guid
     */
    Optional<Actor> findByGuid(UUID guid);

    /**
     * Retrieves an Actor by its user owner.
     *
     * @param user
     * @return the Actor with the given user owner
     */
    Optional<Actor> findByUser(IUser user);

    /**
     * Retrieves an Actor by its user guid.
     *
     * @param guid
     * @return the Actor with the given user owner  or {@literal Optional#empty()} if none found
     */
    Optional<Actor> findByUserGuid(UUID guid);

    /**
     * Retrieves an Actor by tenant, user, and role.
     *
     * @param tenantGuid tenant guid
     * @param userGuid   user guid
     * @param roleName   role name
     * @return actor
     */
    Optional<Actor> findByTenantGuidAndUserGuidAndActorRolesName(UUID tenantGuid, UUID userGuid, String roleName);

    /**
     * Retrieves an Actor by its tenant guid.
     *
     * @param guid
     * @return the List of Actors with the given tenant guid
     */
    List<Actor> findAllByTenantGuid(UUID guid);

}
