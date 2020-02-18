package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import com.softserve.itacademy.kek.models.ITenantProperties;

/**
 * Service interface for {@link ITenantProperties}
 */
public interface ITenantPropertiesService {

    /**
     * Returns all tenantProperties for tenant by tenant guid
     * @param tenantGuid tenant guid
     * @return tenantProperties by tenant guid
     */
    List<ITenantProperties> getAllForTenant(UUID tenantGuid);

    /**
     * Creates tenantProperty for tenant by tenant guid
     * @param tenantProperties tenantProperties for creating
     * @param tenantGuid tenant guid
     * @return created tenantProperties
     */
    List<ITenantProperties> create(List<ITenantProperties> tenantProperties, UUID tenantGuid);

    /**
     * Gets tenantProperty by tenant guid and tenantProperty guid
     * @param tenantGuid tenant guid
     * @param tenantPropertyGuid tenantProperty  guid
     * @return tenantProperty
     */
    ITenantProperties get(UUID tenantGuid, UUID tenantPropertyGuid);

    /**
     * Updates tenantProperty by tenant guid and tenantProperty guid
     * @param tenantGuid tenant guid
     * @param tenantPropertyGuid tenantProperty guid
     * @param iTenantProperty tenantProperty
     * @return updated tenantProperty
     */
    ITenantProperties update(UUID tenantGuid, UUID tenantPropertyGuid, ITenantProperties iTenantProperty);

    /**
     * Deletes tenantProperty by tenant guid and tenantProperty guid
     * @param tenantGuid tenant guid
     * @param tenantPropertyGuid tenantProperty guid
     */
    void delete(UUID tenantGuid, UUID tenantPropertyGuid);

}
