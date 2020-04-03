package com.softserve.itacademy.kek.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.PropertyType;

/**
 * Repository for work with Tenant property types
 */
public interface PropertyTypeRepository extends JpaRepository<PropertyType, Long> {
    /**
     * Retrieves a tenant by its name.
     *
     * @param name
     * @return the tenant properties with the given guid or {@literal Optional#empty()} if none found
     */
    Optional<PropertyType> getByName(String name);
}
