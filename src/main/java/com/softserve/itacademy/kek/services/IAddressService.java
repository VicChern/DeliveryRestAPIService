package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.modelInterfaces.IAddress;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for {@link IAddress}
 */
public interface IAddressService {

    /**
     * Returns all addresses for tenant by tenant guid
     * @param tenantGuid tenant guid
     * @return addresses by tenant guid
     */
    List<IAddress> getAddressesByTenantGuid(UUID tenantGuid);

    /**
     * Creates address for tenant by tenant guid
     * @param address address for creating
     * @param tenantGuid tenant guid
     * @return created address
     */
    IAddress createAddressForTenant(IAddress address, UUID tenantGuid);

    /**
     * Gets address by tenant guid and address guid
     * @param tenantGuid tenant guid
     * @param addressGuid address guid
     * @return address
     */
    IAddress getAddressForTenantByGuid(UUID tenantGuid, UUID addressGuid);

    /**
     * Updates address by tenant guid and address guid
     * @param tenantGuid tenant guid
     * @param addressGuid address guid
     * @return updated address
     */
    IAddress updateAddressForTenantByGuid(UUID tenantGuid, UUID addressGuid, IAddress address);

    /**
     * Deletes address by tenant guid and address guid
     * @param tenantGuid tenant guid
     * @param addressGuid address guid
     */
    void deleteAddressForTenantByGuid(UUID tenantGuid, UUID addressGuid);

    /**
     * Returns all addresses for user by tenant guid
     * @param userGuid user guid
     * @return addresses by user guid
     */
    List<IAddress> getAddressesByUserGuid(UUID userGuid);

    /**
     * Creates address for user by user guid
     * @param address address for creating
     * @param userGuid user guid
     * @return created address
     */
    IAddress createAddressForUser(IAddress address, UUID userGuid);

    /**
     * Gets address by user guid and address guid
     * @param userGuid user guid
     * @param addressGuid address guid
     * @return address
     */
    IAddress getAddressForUserByGuid(UUID userGuid, UUID addressGuid);

    /**
     * Updates address by user guid and address guid
     * @param userGuid user guid
     * @param addressGuid address guid
     * @return updated address
     */
    IAddress updateAddressForUserByGuid(UUID userGuid, UUID addressGuid, IAddress address);

    /**
     * Deletes address by user guid and address guid
     * @param userGuid user guid
     * @param addressGuid address guid
     */
    void deleteAddressForUserByGuid(UUID userGuid, UUID addressGuid);
}
