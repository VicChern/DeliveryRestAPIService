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
     * @param address    address data
     * @param tenantGuid tenant guid
     * @return created tenant address
     * @throws AddressServiceException
     */
    IAddress createForTenant(IAddress address, UUID tenantGuid) throws AddressServiceException;

    /**
     * Updates tenant address
     *
     * @param address    address data
     * @param tenantGuid tenant guid
     * @return updated tenant address
     * @throws AddressServiceException
     */
    IAddress updateForTenant(IAddress address, UUID tenantGuid, UUID addressGuid) throws AddressServiceException;

    /**
     * Deletes tenant address
     *
     * @param addressGuid address guid
     * @param tenantGuid  tenant guid
     * @throws AddressServiceException
     */
    void deleteForTenant(UUID addressGuid, UUID tenantGuid) throws AddressServiceException;

    /**
     * Gets tenant address
     *
     * @param addressGuid address guid
     * @param tenantGuid  tenant guid
     * @return tenant address
     * @throws AddressServiceException
     */
    IAddress getForTenant(UUID addressGuid, UUID tenantGuid) throws AddressServiceException;

    /**
     * Returns all tenant addresses
     *
     * @param tenantGuid tenant guid
     * @return tenant addresses
     * @throws AddressServiceException
     */
    List<IAddress> getAllForTenant(UUID tenantGuid) throws AddressServiceException;

    /**
     * Creates user address
     *
     * @param address  address data
     * @param userGuid user guid
     * @return created user address
     * @throws AddressServiceException
     */
    IAddress createForUser(IAddress address, UUID userGuid) throws AddressServiceException;

    /**
     * Updates user address
     *
     * @param address  address data
     * @param userGuid user guid
     * @return updated user address
     * @throws AddressServiceException
     */
    IAddress updateForUser(IAddress address, UUID userGuid) throws AddressServiceException;

    /**
     * Deletes user address
     *
     * @param addressGuid address guid
     * @param userGuid    user guid
     * @throws AddressServiceException
     */
    void deleteForUser(UUID addressGuid, UUID userGuid) throws AddressServiceException;

    /**
     * Returns user address
     *
     * @param addressGuid address guid
     * @param userGuid    user guid
     * @return user address
     * @throws AddressServiceException
     */
    IAddress getForUser(UUID addressGuid, UUID userGuid) throws AddressServiceException;

    /**
     * Returns all user addresses
     *
     * @param userGuid user guid
     * @return user addresses
     * @throws AddressServiceException
     */
    List<IAddress> getAllForUser(UUID userGuid) throws AddressServiceException;
}
