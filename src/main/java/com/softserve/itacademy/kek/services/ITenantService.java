package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.objects.TenantObject;

import java.util.UUID;

/**
 * Service interface for {@link Tenant} and {@link TenantObject}
 */
public interface ITenantService {

    /**
     * Saved new {@link Tenant} to db
     * @param tenantObject tenant object that converted to {@link Tenant} in this method
     * @return converted (from saved to db {@link Tenant}) tenant object
     */
    TenantObject save(TenantObject tenantObject);

    /**
     * Gets an tenant object for principal user
     * @return converted (from saved to db tenant) tenant object for principal user
     */
    TenantObject get();

    /**
     * Gets tenant object by {@link Tenant} guid
     * @param guid for that the {@link Tenant} is being getting
     * @return converted (from getting from db {@link Tenant}) tenant object for {@link Tenant} guid
     */
    TenantObject getByGuid(UUID guid);

    /**
     * Updates {@link Tenant}
     * @param tenantObject
     * @param guid {@link Tenant} guid
     * @return converted (from updated {@link Tenant}) tenant object for {@link Tenant} guid
     */
    TenantObject update(TenantObject tenantObject, UUID guid);

    /**
     * Deletes {@link Tenant} by guid
     * @param guid must not be {@literal null}.
     */
    void deleteByGuid(UUID guid);

}
