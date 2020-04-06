package com.softserve.itacademy.kek.models;

import java.util.UUID;

import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.dto.TenantPropertiesDto;

/**
 * Interface for Address data exchange with service and public api layers
 */
public interface IAddress {

    /**
     * Returns address GUID
     *
     * @return address GUID
     */
    UUID getGuid();

    /**
     * Returns address
     *
     * @return address
     */
    String getAddress();

    /**
     * Returns address notes
     *
     * @return address notes
     */
    String getNotes();

    /**
     * Returns address alias
     *
     * @return address alias
     */
    String getAlias();

    /**
     * Transform {@link ITenantProperties} to {@link TenantPropertiesDto}
     *
     * @return new AddressDto
     */
    default AddressDto transformAddress() {
        return new AddressDto(getGuid(), getAlias(), getAddress(), getNotes());
    }
}
