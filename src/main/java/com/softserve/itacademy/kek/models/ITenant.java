package com.softserve.itacademy.kek.models;

import java.util.UUID;

/**
 * Interface for exchange data with tenant service layer
 */
public interface ITenant {

    /**
     * Returns tenant GUID
     *
     * @return tenant GUID
     */
    UUID getGuid();

    /**
     * Returns tenant name
     *
     * @return tenant name
     */
    String getName();

    /**
     * Returns tenant owner
     *
     * @return tenant owner
     */
    IUser getTenantOwner();

    /**
     * Returns tenant details
     *
     * @return tenant details
     */
    ITenantDetails getTenantDetails();

}
