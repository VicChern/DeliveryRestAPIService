package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import com.softserve.itacademy.kek.exception.TenantPropertiesServiceException;
import com.softserve.itacademy.kek.models.ITenantProperties;

/**
 * Service interface for {@link ITenantProperties}
 */
public interface ITenantPropertiesService {

    /**
     * Saved tenantProperty for tenant by tenant guid to db
     *
     * @param tenantProperties tenant properties
     * @param tenantGuid       tenant guid
     * @return created tenantProperties
     * @throws TenantPropertiesServiceException if tenant properties wasn't saved
     */
    List<ITenantProperties> create(List<ITenantProperties> tenantProperties, UUID tenantGuid) throws TenantPropertiesServiceException;

    /**
     * Gets tenantProperty by tenant guid and tenantProperty guid
     *
     * @param tenantGuid         tenant guid
     * @param tenantPropertyGuid tenantProperty  guid
     * @return tenantProperty
     * @throws TenantPropertiesServiceException if there is no one tenant properties with tenant guid and tenantProperty guid
     */
    ITenantProperties getPropertyByTenantGuid(UUID tenantGuid, UUID tenantPropertyGuid) throws TenantPropertiesServiceException;

    /**
     * Gets all tenantProperties for tenant by tenant guid
     *
     * @param tenantGuid tenant guid
     * @return tenantProperties by tenant guid
     * @throws TenantPropertiesServiceException if an error occurred
     */
    List<ITenantProperties> getAllForTenant(UUID tenantGuid) throws TenantPropertiesServiceException;

    /**
     * Updates tenantProperty by tenant guid and tenantProperty guid
     *
     * @param tenantGuid         tenant guid
     * @param tenantPropertyGuid tenantProperty guid
     * @param iTenantProperty    tenantProperty
     * @return updated tenantProperty
     * @throws TenantPropertiesServiceException if tenant properties wasn't updated
     */
    ITenantProperties update(UUID tenantGuid, UUID tenantPropertyGuid, ITenantProperties iTenantProperty) throws TenantPropertiesServiceException;

    /**
     * Deletes tenantProperty by tenant guid and tenantProperty guid
     *
     * @param tenantGuid         tenant guid
     * @param tenantPropertyGuid tenantProperty guid
     * @throws TenantPropertiesServiceException if there is no one tenant with guid for deleting
     */
    void delete(UUID tenantGuid, UUID tenantPropertyGuid) throws TenantPropertiesServiceException;

}
