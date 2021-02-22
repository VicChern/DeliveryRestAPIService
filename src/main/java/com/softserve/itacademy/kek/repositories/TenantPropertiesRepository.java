package com.softserve.itacademy.kek.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.TenantProperties;

/**
 * Repository for work with Tenant properties
 */
public interface TenantPropertiesRepository extends JpaRepository<TenantProperties, Long> {

    /**
     * Retrieves a tenant property by its guid.
     *
     * @param guid tenant property guid
     * @return the tenant properties with the given guid or {@literal Optional#empty()} if none found
     */
    Optional<TenantProperties> findByGuid(UUID guid);
}
