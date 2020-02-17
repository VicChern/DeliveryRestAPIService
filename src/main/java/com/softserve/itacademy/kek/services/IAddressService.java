package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import com.softserve.itacademy.kek.modelInterfaces.IAddress;

/**
 * Service interface for {@link IAddress}
 */
public interface IAddressService {

    /**
     * Creates tenant address
     *
     * @param addressData address data
     * @param tenantGuid  tenant guid
     * @return created tenant address
     */
    IAddress createAddressForTenant(IAddress addressData, UUID tenantGuid);

    /**
     * Updates tenant address
     *
     * @param addressData address data
     * @param tenantGuid  tenant guid
     * @return updated tenant address
     */
    IAddress updateAddressForTenant(IAddress addressData, UUID tenantGuid);

    /**
     * Deletes tenant address
     *
     * @param addressGuid address guid
     * @param tenantGuid  tenant guid
     */
    void deleteAddressForTenant(UUID addressGuid, UUID tenantGuid);

    /**
     * Gets tenant address
     *
     * @param addressGuid address guid
     * @param tenantGuid  tenant guid
     * @return tenant address
     */
    IAddress getAddressForTenant(UUID addressGuid, UUID tenantGuid);

    /**
     * Returns all tenant addresses
     *
     * @param tenantGuid tenant guid
     * @return tenant addresses
     */
    List<IAddress> getAddressAllForTenant(UUID tenantGuid);

    /**
     * Creates user address
     *
     * @param addressData address data
     * @param userGuid    user guid
     * @return created user address
     */
    IAddress createAddressForUser(IAddress addressData, UUID userGuid);

    /**
     * Updates user address
     *
     * @param addressData address data
     * @param userGuid    user guid
     * @return updated user address
     */
    IAddress updateAddressForUser(IAddress addressData, UUID userGuid);

    /**
     * Deletes user address
     *
     * @param addressGuid address guid
     * @param userGuid    user guid
     */
    void deleteAddressForUser(UUID addressGuid, UUID userGuid);

    /**
     * Returns user address
     *
     * @param addressGuid address guid
     * @param userGuid    user guid
     * @return user address
     */
    IAddress getAddressForUser(UUID addressGuid, UUID userGuid);

    /**
     * Returns all user addresses
     *
     * @param userGuid user guid
     * @return user addresses
     */
    List<IAddress> getAddressAllForUser(UUID userGuid);
}
