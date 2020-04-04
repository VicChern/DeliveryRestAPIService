package com.softserve.itacademy.kek.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.Actor;

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
     * Retrieves an Actor by its tenant guid.
     *
     * @param guid
     * @return the List of Actors with the given tenant guid
     */
    List<Actor> findAllByTenantGuid(UUID guid);

}
