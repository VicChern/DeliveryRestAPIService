package com.softserve.itacademy.kek.services.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.AddressServiceException;
import com.softserve.itacademy.kek.mappers.IAddressMapper;
import com.softserve.itacademy.kek.models.IAddress;
import com.softserve.itacademy.kek.models.impl.Address;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.AddressRepository;
import com.softserve.itacademy.kek.services.IAddressService;
import com.softserve.itacademy.kek.services.ITenantService;
import com.softserve.itacademy.kek.services.IUserService;

/**
 * Service implementation for {@link IAddressService}
 */
@Service
public class AddressServiceImpl implements IAddressService {
    private final static Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final ITenantService tenantService;
    private final IUserService userService;
    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(ITenantService tenantService,
                              IUserService userService,
                              AddressRepository addressRepository) {
        this.tenantService = tenantService;
        this.userService = userService;
        this.addressRepository = addressRepository;
    }

    @Transactional
    @Override
    public IAddress createForTenant(UUID tenantGuid, IAddress address) throws AddressServiceException {
        logger.info("Insert tenant address into DB: tenantGuid = {}, address = {}", tenantGuid, address);

        try {
            Address actualAddress = IAddressMapper.INSTANCE.toAddress(address);
            actualAddress.setGuid(UUID.randomUUID());

            final Tenant tenant = (Tenant) tenantService.getByGuid(tenantGuid);
            actualAddress.setTenant(tenant);

            final Address insertedAddress = addressRepository.saveAndFlush(actualAddress);

            logger.debug("Tenant address was inserted into DB: tenantGuid = {}, insertedAddress = {}",
                    tenantGuid, insertedAddress);

            return insertedAddress;
        } catch (Exception ex) {
            logger.error("Error while inserting tenant address into DB: " + address, ex);
            throw new AddressServiceException("An error occurred while inserting tenant address", ex);
        }
    }

    @Transactional
    @Override
    public IAddress updateForTenant(UUID addressGuid, UUID tenantGuid, IAddress address) throws AddressServiceException {
        logger.info("Update tenant address in DB: addressGuid = {}, tenantGuid = {}, address = {}",
                addressGuid, tenantGuid, address);

        final Address actualAddress = (Address) getForTenant(addressGuid, tenantGuid);

        try {
            actualAddress.setAddress(address.getAddress());
            actualAddress.setAlias(address.getAlias());
            actualAddress.setNotes(address.getNotes());

            final Address updatedAddress = addressRepository.saveAndFlush(actualAddress);

            logger.debug("Tenant address was updated in DB: {}", updatedAddress);

            return updatedAddress;
        } catch (Exception ex) {
            logger.error("Error while updating tenant address in DB: " + actualAddress, ex);
            throw new AddressServiceException("An error occurred while updating tenant address", ex);
        }
    }

    @Transactional
    @Override
    public void deleteForTenant(UUID addressGuid, UUID tenantGuid) throws AddressServiceException {
        logger.info("Delete tenant address from DB: tenantGuid = {}, addressGuid = {}", tenantGuid, addressGuid);

        final Address address = (Address) getForTenant(addressGuid, tenantGuid);

        try {
            addressRepository.deleteById(address.getIdAddress());
            addressRepository.flush();

            logger.debug("Tenant address was deleted from DB: tenantGuid = {}, deletedAddress = {}", tenantGuid, address);
        } catch (Exception ex) {
            logger.error("Error while deleting tenant address from DB", ex);
            throw new AddressServiceException("An error occurred while deleting tenant address", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IAddress getForTenant(UUID addressGuid, UUID tenantGuid) throws AddressServiceException {
        logger.info("Get tenant address from DB: tenantGuid = {}, addressGuid = {}", tenantGuid, addressGuid);

        try {
            final Address address = addressRepository.findByGuidAndTenantGuid(addressGuid, tenantGuid)
                    .orElseThrow(() -> new NoSuchElementException("Tenant address was not found"));

            logger.debug("Tenant address was read from DB: {}", address);

            return address;
        } catch (Exception ex) {
            logger.error("Error while getting tenant address from DB", ex);
            throw new AddressServiceException("An error occurred while getting tenant address", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<IAddress> getAllForTenant(UUID tenantGuid) throws AddressServiceException {
        logger.info("Get tenant addresses from DB: tenantGuid = {}", tenantGuid);

        try {
            final List<? extends IAddress> addresses = addressRepository.findAllByTenantGuid(tenantGuid);

            logger.debug("Tenant addresses were read from DB: tenantGuid = {}", tenantGuid);

            return (List<IAddress>) addresses;
        } catch (Exception ex) {
            logger.error("Error while getting tenant addresses from DB", ex);
            throw new AddressServiceException("An error occurred while getting tenant addresses", ex);
        }
    }

    @Transactional
    @Override
    public IAddress createForUser(UUID userGuid, IAddress address) throws AddressServiceException {
        logger.info("Insert user address into DB: userGuid = {}, address = {}", userGuid, address);

        try {
            final Address actualAddress = new Address();
            actualAddress.setGuid(UUID.randomUUID());
            actualAddress.setAddress(address.getAddress());
            actualAddress.setAlias(address.getAlias());
            actualAddress.setNotes(address.getNotes());

            final User user = (User) userService.getByGuid(userGuid);
            actualAddress.setUser(user);

            final Address insertedAddress = addressRepository.saveAndFlush(actualAddress);

            logger.debug("User address was inserted into DB: insertedAddress = {}", insertedAddress);

            return insertedAddress;
        } catch (Exception ex) {
            logger.error("Error while inserting user address into DB", ex);
            throw new AddressServiceException("An error occurred while inserting user address", ex);
        }
    }

    @Transactional
    @Override
    public IAddress updateForUser(UUID addressGuid, UUID userGuid, IAddress address) throws AddressServiceException {
        logger.info("Update user address in DB: addressGuid = {}, userGuid = {}, address = {}",
                addressGuid, userGuid, address);

        final Address actualAddress = (Address) getForUser(addressGuid, userGuid);

        try {
            actualAddress.setAddress(address.getAddress());
            actualAddress.setAlias(address.getAlias());
            actualAddress.setNotes(address.getNotes());

            final Address updatedAddress = addressRepository.saveAndFlush(actualAddress);

            logger.debug("User address was updated in DB: {}", updatedAddress);

            return updatedAddress;
        } catch (Exception ex) {
            logger.error("Error while updating user address in DB: userGuid = " + userGuid, ex);
            throw new AddressServiceException("An error occurred while updating user address", ex);
        }
    }

    @Transactional
    @Override
    public void deleteForUser(UUID addressGuid, UUID userGuid) throws AddressServiceException {
        logger.info("Delete user address from DB: userGuid = {}, addressGuid = {}", userGuid, addressGuid);

        final Address address = (Address) getForUser(addressGuid, userGuid);

        try {
            addressRepository.deleteById(address.getIdAddress());
            addressRepository.flush();

            logger.debug("User address was deleted from DB: {}", address);
        } catch (Exception ex) {
            logger.error("Error while deleting user address from DB: addressGuid = " + addressGuid, ex);
            throw new AddressServiceException("An error occurred while deleting user address", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IAddress getForUser(UUID addressGuid, UUID userGuid) throws AddressServiceException {
        logger.info("Get user address from DB: userGuid = {}, addressGuid = {}", userGuid, addressGuid);

        try {
            final Address address = addressRepository.findByGuidAndUserGuid(addressGuid, userGuid)
                    .orElseThrow(() -> new NoSuchElementException("User address was not found"));

            logger.debug("User address was gotten from DB: {}", address);

            return address;
        } catch (Exception ex) {
            logger.error("Error while getting user address from DB", ex);
            throw new AddressServiceException("An error occurred while getting user address", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<IAddress> getAllForUser(UUID userGuid) throws AddressServiceException {
        logger.info("Get user addresses: userGuid = {}", userGuid);

        try {
            final List<? extends IAddress> addresses = addressRepository.findAllByUserGuid(userGuid);

            logger.debug("User addresses was gotten from DB: userGuid = {}", userGuid);

            return (List<IAddress>) addresses;
        } catch (DataAccessException ex) {
            logger.error("Error while getting user addresses from DB: userGuid = " + userGuid, ex);
            throw new AddressServiceException("An error occurred while getting user addresses", ex);
        }
    }
}
