package com.softserve.itacademy.kek.services.impl;

import javax.validation.ConstraintViolationException;
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
    public IAddress createForTenant(IAddress address, UUID tenantGuid) throws AddressServiceException {
        logger.info("Insert tenant address into DB: tenantGuid = {}, address = {}", tenantGuid, address.getAddress());

        final Address actualAddress = new Address();
        final Tenant tenant = (Tenant) tenantService.getByGuid(tenantGuid);

        actualAddress.setGuid(UUID.randomUUID());
        actualAddress.setAddress(address.getAddress());
        actualAddress.setAlias(address.getAlias());
        actualAddress.setNotes(address.getNotes());
        actualAddress.setTenant(tenant);

        try {
            final Address insertedAddress = addressRepository.saveAndFlush(actualAddress);

            logger.debug("Tenant address was inserted into DB: tenantGuid = {}, insertedAddress = {}",
                    tenantGuid, insertedAddress);

            return insertedAddress;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while inserting tenant address into DB: " + actualAddress, ex);
            throw new AddressServiceException("An error occurred while inserting tenant address", ex);
        }
    }

    @Transactional
    @Override
    public IAddress updateForTenant(IAddress address, UUID tenantGuid, UUID addressGuid) throws AddressServiceException {
        logger.info("Update tenant address in DB: tenantGuid = {}, addressGuid = {}, address = {}",
                tenantGuid, addressGuid, address.getAddress());

        final Address actualAddress = findAddressByGuid(address.getGuid());
        final Tenant tenant = (Tenant) tenantService.getByGuid(tenantGuid);

        checkAddressBelongsTenant(actualAddress, tenant);

        actualAddress.setAddress(address.getAddress());
        actualAddress.setAlias(address.getAlias());
        actualAddress.setNotes(address.getNotes());

        try {
            final Address updatedAddress = addressRepository.saveAndFlush(actualAddress);

            logger.debug("Tenant address was updated in DB: tenantGuid = {}, updatedAddress = {}",
                    tenantGuid, updatedAddress);

            return updatedAddress;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while updating tenant address in DB: " + actualAddress, ex);
            throw new AddressServiceException("An error occurred while updating tenant address", ex);
        }
    }

    @Transactional
    @Override
    public void deleteForTenant(UUID addressGuid, UUID tenantGuid) throws AddressServiceException {
        logger.info("Delete tenant address from DB: tenantGuid = {}, addressGuid = {}", tenantGuid, addressGuid);

        final Address address = findAddressByGuid(addressGuid);
        final Tenant tenant = (Tenant) tenantService.getByGuid(tenantGuid);

        checkAddressBelongsTenant(address, tenant);

        try {
            addressRepository.deleteById(address.getIdAddress());
            addressRepository.flush();

            logger.debug("Tenant address was deleted from DB: tenantGuid = {}, deletedAddress = {}", tenantGuid, address);
        } catch (DataAccessException ex) {
            logger.error(String.format("Error while deleting tenant address from DB: tenantGuid = %s, address = %s",
                    tenantGuid, address), ex);
            throw new AddressServiceException("An error occurred while deleting tenant address", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IAddress getForTenant(UUID addressGuid, UUID tenantGuid) throws AddressServiceException {
        logger.info("Get tenant address from DB: tenantGuid = {}, addressGuid = {}", tenantGuid, addressGuid);

        final Address address = findAddressByGuid(addressGuid);
        final Tenant tenant = (Tenant) tenantService.getByGuid(tenantGuid);

        checkAddressBelongsTenant(address, tenant);

        return address;
    }

    @Transactional(readOnly = true)
    @Override
    public List<IAddress> getAllForTenant(UUID tenantGuid) throws AddressServiceException {
        logger.info("Get tenant addresses from DB: tenantGuid = {}", tenantGuid);

        try {
            final List<? extends IAddress> addresses = addressRepository.findAllByTenantGuid(tenantGuid);

            logger.debug("Tenant addresses was gotten from DB: tenantGuid = {}", tenantGuid);

            return (List<IAddress>) addresses;
        } catch (DataAccessException ex) {
            logger.error("Error while getting tenant addresses from DB: tenantGuid = " + tenantGuid, ex);
            throw new AddressServiceException("An error occurred while getting tenant addresses", ex);
        }
    }

    @Transactional
    @Override
    public IAddress createForUser(IAddress address, UUID userGuid) throws AddressServiceException {
        logger.info("Insert user address into DB: userGuid = {}, address = {}", userGuid, address.getAddress());

        final Address actualAddress = new Address();
        final User user = (User) userService.getByGuid(userGuid);

        actualAddress.setGuid(UUID.randomUUID());
        actualAddress.setAddress(address.getAddress());
        actualAddress.setAlias(address.getAlias());
        actualAddress.setNotes(address.getNotes());
        actualAddress.setUser(user);

        try {
            final Address insertedAddress = addressRepository.saveAndFlush(actualAddress);

            logger.debug("User address was inserted into DB: userGuid = {}, insertedAddress = {}",
                    userGuid, insertedAddress);

            return insertedAddress;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while inserting user address into DB: " + actualAddress, ex);
            throw new AddressServiceException("An error occurred while inserting user address", ex);
        }
    }

    @Transactional
    @Override
    public IAddress updateForUser(IAddress address, UUID userGuid) throws AddressServiceException {
        logger.info("Update user address in DB: userGuid = {}, addressGuid = {}", userGuid, address.getGuid());

        final Address actualAddress = findAddressByGuid(address.getGuid());
        final User user = (User) userService.getByGuid(userGuid);

        checkAddressBelongsUser(actualAddress, user);

        actualAddress.setAddress(address.getAddress());
        actualAddress.setAlias(address.getAlias());
        actualAddress.setNotes(address.getNotes());

        try {
            final Address updatedAddress = addressRepository.saveAndFlush(actualAddress);

            logger.debug("User address was updated in DB: userGuid = {}, address = {}", userGuid, updatedAddress);

            return updatedAddress;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while updating user address in DB: " + actualAddress, ex);
            throw new AddressServiceException("An error occurred while updating user address", ex);
        }
    }

    @Transactional
    @Override
    public void deleteForUser(UUID addressGuid, UUID userGuid) throws AddressServiceException {
        logger.info("Delete user address from DB: userGuid = {}, addressGuid = {}", userGuid, addressGuid);

        final Address address = findAddressByGuid(addressGuid);
        final User user = (User) userService.getByGuid(userGuid);

        checkAddressBelongsUser(address, user);

        try {
            addressRepository.deleteById(address.getIdAddress());
            addressRepository.flush();

            logger.debug("User address was deleted from DB: userGuid = {}, addressGuid = {}", userGuid, addressGuid);
        } catch (DataAccessException ex) {
            logger.error("Error while deleting user address from DB: " + address, ex);
            throw new AddressServiceException("An error occurred while deleting user address", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IAddress getForUser(UUID addressGuid, UUID userGuid) throws AddressServiceException {
        logger.info("Get user address from DB: userGuid = {}, addressGuid = {}", userGuid, addressGuid);

        Address address = findAddressByGuid(addressGuid);
        User user = (User) userService.getByGuid(userGuid);

        checkAddressBelongsUser(address, user);

        return address;
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

    private Address findAddressByGuid(UUID guid) {
        logger.info("Find address in DB: guid = {}", guid);

        try {
            Address address = addressRepository.findByGuid(guid).orElseThrow(() -> {
                logger.error("Address wasn't found in DB: guid = {}", guid);
                return new AddressServiceException("Address was not found in database for guid: " + guid, new NoSuchElementException());
            });
            return address;

        } catch (DataAccessException ex) {
            logger.error("Error while getting address from DB: guid = " + guid, ex);
            throw new AddressServiceException("An error occurred while getting address", ex);
        }
    }

    private void checkAddressBelongsTenant(Address address, Tenant tenant) {
        Tenant addressTenant = address.getTenant();

        if ((addressTenant == null) || (!addressTenant.getGuid().equals(tenant.getGuid()))) {
            logger.error("Address does not belong to Tenant: tenant.guid = {}, address = {}", tenant.getGuid(), address);
            throw new AddressServiceException("Address does not belong to tenant");
        }
    }

    private void checkAddressBelongsUser(Address address, User user) {
        User addressUser = address.getUser();

        if ((addressUser == null) || (!addressUser.getGuid().equals(user.getGuid()))) {
            logger.error("Address does not belong to User: user.guid = {}, address = {}", user.getGuid(), address);
            throw new AddressServiceException("Address does not belong to user");
        }
    }
}
