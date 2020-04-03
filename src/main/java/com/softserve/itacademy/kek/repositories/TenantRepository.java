package com.softserve.itacademy.kek.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.Tenant;

/**
 * Repository for work with tenant
 */
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    /**
     * Retrieves a tenant by its guid.
     *
     * @param guid
     * @return the user with the given guid or {@literal Optional#empty()} if none found
     */
    Optional<Tenant> findByGuid(UUID guid);

    /**
     * Retrieves a tenant by its guid.
     *
     * @param user owner
     * @return the tenant with the given guid
     */
    Optional <Tenant> findByTenantOwner(IUser user);

}
