package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.modelInterfaces.ITenant;

import java.util.UUID;

/**
 * Service interface for {@link ITenant}
 */
public interface ITenantService {

    /**
     * Saved new {@link Tenant} to db
     * @param tenant tenant
     * @return saved tenant
     */
    ITenant save(ITenant tenant);

    /**
     * Gets an tenant for principal user
     * @return tenant for principal user
     */
    ITenant get();

    /**
     * Gets tenant by {@link Tenant} guid
     * @param guid {@link Tenant} guid
     * @return tenant
     */
    ITenant getByGuid(UUID guid);

    /**
     * Updates {@link Tenant}
     * @param tenant
     * @param guid {@link Tenant} guid
     * @return updated tenant
     */
    ITenant update(ITenant tenant, UUID guid);

    /**
     * Deletes {@link Tenant} by guid
     * @param guid must not be {@literal null}.
     */
    void deleteByGuid(UUID guid);

}
