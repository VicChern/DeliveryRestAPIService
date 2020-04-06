package com.softserve.itacademy.kek.models;

import java.util.UUID;

import com.softserve.itacademy.kek.dto.TenantDetailsDto;
import com.softserve.itacademy.kek.dto.TenantDto;

/**
 * Interface for exchange data with tenant service and public api layers
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

    /**
     * Default method to get tenant owner guid
     *
     * @return tenant owner guid
     */
    default UUID getOwner() {
        return getTenantOwner().getGuid();
    }

    /**
     * Transform {@link ITenant} to {@link TenantDto}
     *
     * @return tenantDto
     */
    default TenantDto transform() {
        TenantDetailsDto tenantDetailsDto = new TenantDetailsDto(getTenantDetails().getPayload(), getTenantDetails().getImageUrl());
        TenantDto tenantDto = new TenantDto(getGuid(), getOwner(), getName(), tenantDetailsDto);
        return tenantDto;
    }
}
