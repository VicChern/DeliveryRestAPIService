package com.softserve.itacademy.kek.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.ActorRole;

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
    ActorRole findByName(String name);
}
