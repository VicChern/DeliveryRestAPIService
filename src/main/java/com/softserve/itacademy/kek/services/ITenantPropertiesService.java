package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.modelInterfaces.ITenantProperties;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for {@link ITenantProperties}
 */
public interface ITenantPropertiesService {

    /**
     * Returns all tenantProperties for tenant by tenant guid
     * @param tenantGuid tenant guid
     * @return tenantProperties by tenant guid
     */
    List<ITenantProperties> getTenantPropertiesByTenantGuid(UUID tenantGuid);

    /**
     * Creates tenantProperty for tenant by tenant guid
     * @param tenantProperties tenantProperties for creating
     * @param tenantGuid tenant guid
     * @return created tenantProperties
     */
    List<ITenantProperties> createTenantPropertiesForTenant(List<ITenantProperties> tenantProperties, UUID tenantGuid);

    /**
     * Gets tenantProperty by tenant guid and tenantProperty guid
     * @param tenantGuid tenant guid
     * @param tenantPropertyGuid tenantProperty  guid
     * @return tenantProperty
     */
    ITenantProperties getTenantPropertyForTenantByGuid(UUID tenantGuid, UUID tenantPropertyGuid);

    /**
     * Updates tenantProperty by tenant guid and tenantProperty guid
     * @param tenantGuid tenant guid
     * @param tenantPropertyGuid tenantProperty guid
     * @param tenantProperty tenantProperty
     * @return updated tenantProperty
     */
    ITenantProperties updateTenantPropertyForTenantByGuid(UUID tenantGuid, UUID tenantPropertyGuid, ITenantProperties tenantProperty);

    /**
     * Deletes tenantProperty by tenant guid and tenantProperty guid
     * @param tenantGuid tenant guid
     * @param tenantProperty tenantProperty guid
     */
    void deleteTenantPropertyForTenantByGuid(UUID tenantGuid, UUID tenantProperty);

}
