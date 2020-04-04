package com.softserve.itacademy.kek.models;

/**
 * Interface for Tenant details data exchange with service and public api layers
 */
public interface ITenantDetails {

    /**
     * Returns tenant payload
     *
     * @return tenant payload
     */
    String getPayload();

    /**
     * Returns tenant image url
     *
     * @return tenant image url
     */
    String getImageUrl();
}
