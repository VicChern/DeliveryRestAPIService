package com.softserve.itacademy.kek.modelInterfaces;

/**
 * Interface for Tenant details data exchange with service layer
 */
public interface ITenantDetails {

    /**
     * Returns tenant payload
     * @return tenant payload
     */
    String getPayload();

    /**
     * Returns tenant image url
     * @return tenant image url
     */
    String getImageUrl();
}
