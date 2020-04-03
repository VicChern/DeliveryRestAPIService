package com.softserve.itacademy.kek.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.IdentityType;

/**
 * Repository for work with Identity Types
 */
public interface IdentityTypeRepository extends JpaRepository<IdentityType, Long> {

    /**
     * Retrieves an Identity Types by its name.
     *
     * @param name
     * @return the Identity Type with the given order
     */
    IdentityType findByName(String name);
}
