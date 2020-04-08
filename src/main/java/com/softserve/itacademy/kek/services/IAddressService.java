package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import com.softserve.itacademy.kek.exception.AddressServiceException;
import com.softserve.itacademy.kek.models.IAddress;

/**
 * Service interface for {@link IAddress}
 */
public interface IAddressService {

    /**
     * Creates tenant address
     *
     * @param tenantGuid tenant guid
     * @param address    address data
     * @return created tenant address
     * @throws AddressServiceException if an error occurred
     */
    IAddress createForTenant(UUID tenantGuid, IAddress address) throws AddressServiceException;

    /**
     * Updates tenant address
     *
     * @param addressGuid address guid
     * @param tenantGuid  tenant guid
     * @param address     address data
     * @return updated tenant address
     * @throws AddressServiceException if an error occurred
     */
    IAddress updateForTenant(UUID addressGuid, UUID tenantGuid, IAddress address) throws AddressServiceException;

    /**
     * Deletes tenant address
     *
     * @param addressGuid address guid
     * @param tenantGuid  tenant guid
     * @throws AddressServiceException if an error occurred
     */
    void deleteForTenant(UUID addressGuid, UUID tenantGuid) throws AddressServiceException;

    /**
     * Gets tenant address
     *
     * @param addressGuid address guid
     * @param tenantGuid  tenant guid
     * @return tenant address
     * @throws AddressServiceException if an error occurred
     */
    IAddress getForTenant(UUID addressGuid, UUID tenantGuid) throws AddressServiceException;

    /**
     * Returns all tenant addresses
     *
     * @param tenantGuid tenant guid
     * @return tenant addresses
     * @throws AddressServiceException if an error occurred
     */
    List<IAddress> getAllForTenant(UUID tenantGuid) throws AddressServiceException;

    /**
     * Creates user address
     *
     * @param userGuid user guid
     * @param address  address data
     * @return created user address
     * @throws AddressServiceException if an error occurred
     */
    IAddress createForUser(UUID userGuid, IAddress address) throws AddressServiceException;

    /**
     * Updates user address
     *
     * @param addressGuid user guid
     * @param userGuid    user guid
     * @param address     address data
     * @return updated user address
     * @throws AddressServiceException if an error occurred
     */
    IAddress updateForUser(UUID addressGuid, UUID userGuid, IAddress address) throws AddressServiceException;

    /**
     * Deletes user address
     *
     * @param addressGuid address guid
     * @param userGuid    user guid
     * @throws AddressServiceException if an error occurred
     */
    void deleteForUser(UUID addressGuid, UUID userGuid) throws AddressServiceException;

    /**
     * Returns user address
     *
     * @param addressGuid address guid
     * @param userGuid    user guid
     * @return user address
     * @throws AddressServiceException if an error occurred
     */
    IAddress getForUser(UUID addressGuid, UUID userGuid) throws AddressServiceException;

    /**
     * Returns all user addresses
     *
     * @param userGuid user guid
     * @return user addresses
     * @throws AddressServiceException if an error occurred
     */
    List<IAddress> getAllForUser(UUID userGuid) throws AddressServiceException;
}
