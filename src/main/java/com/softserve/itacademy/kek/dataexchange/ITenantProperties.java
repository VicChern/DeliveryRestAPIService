package com.softserve.itacademy.kek.dataexchange;

import java.util.UUID;

/**
 * Interface for TenantProperties data exchange with service layer
 */
public interface ITenantProperties {

    /**
     * Returns TenantProperty guid
     *
     * @return TenantProperty guid
     */
    UUID getGuid();

    /**
     * Returns ITenant for TenantProperty
     *
     * @return ITenant for TenantProperty
     */
    ITenant getTenant();

    /**
     * Returns IEventType for TenantProperty
     *
     * @return IEventType for TenantProperty
     */
    IPropertyType getPropertyType();

    /**
     * Returns TenantProperty key
     *
     * @return TenantProperty key
     */
    String getKey();

    /**
     * Returns TenantProperty value
     *
     * @return TenantProperty value
     */
    String getValue();
}
