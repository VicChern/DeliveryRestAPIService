package com.softserve.itacademy.kek.services.impl;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.AddressServiceException;
import com.softserve.itacademy.kek.exception.UserServiceException;
import com.softserve.itacademy.kek.modelInterfaces.IAddress;
import com.softserve.itacademy.kek.models.Address;
import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.models.User;
import com.softserve.itacademy.kek.repositories.AddressRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IAddressService;

/**
 * Service implementation for {@link IAddressService}
 */
@Service
public class AddressServiceImpl implements IAddressService {
    private final static Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(TenantRepository tenantRepository,
                              UserRepository userRepository,
                              AddressRepository addressRepository) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    @Override
    public IAddress createAddressForTenant(IAddress addressData, UUID tenantGuid) {
        logger.info("Insert Tenant address into DB: tenant.guid = {}, address = {}", tenantGuid, addressData);

        Address address = new Address();
        Tenant tenant = findTenantByGuid(tenantGuid);

        address.setGuid(UUID.randomUUID());
        address.setAddress(addressData.getAddress());
        address.setAlias(addressData.getAlias());
        address.setNotes(addressData.getNotes());
        address.setTenant(tenant);

        try {
            address = addressRepository.save(address);
        } catch (PersistenceException | ConstraintViolationException ex) {
            logger.error("Tenant address wasn't inserted into DB: " + address, ex);
            throw new UserServiceException("Tenant address wasn't inserted");
        }

        logger.info("Tenant address was inserted into DB: tenant.guid = {}, address = {}", tenantGuid, address);

        return address;
    }

    @Transactional
    @Override
    public IAddress updateAddressForTenant(IAddress addressData, UUID tenantGuid) {
        logger.info("Update Tenant address in DB: tenant.guid = {}, address = {}", tenantGuid, addressData);

        Address address = findAddressByGuid(addressData.getGuid());
        Tenant tenant = findTenantByGuid(tenantGuid);

        checkAddressBelongsTenant(address, tenant);

        address.setAddress(addressData.getAddress());
        address.setAlias(addressData.getAlias());
        address.setNotes(addressData.getNotes());

        try {
            address = addressRepository.save(address);
        } catch (PersistenceException | ConstraintViolationException ex) {
            logger.error("Tenant address wasn't updated in DB: " + address, ex);
            throw new UserServiceException("Tenant address wasn't updated");
        }

        logger.info("Tenant address was updated in DB: tenant.guid = {}, address = {}", tenantGuid, address);

        return address;
    }

    @Transactional
    @Override
    public void deleteAddressForTenant(UUID addressGuid, UUID tenantGuid) {
        logger.info("Delete Tenant address from DB: tenant.guid = {}, address.guid = {}", tenantGuid, addressGuid);

        Address address = findAddressByGuid(addressGuid);
        Tenant tenant = findTenantByGuid(tenantGuid);

        checkAddressBelongsTenant(address, tenant);

        try {
            addressRepository.deleteById(address.getIdAddress());
        } catch (PersistenceException ex) {
            logger.error("Tenant address wasn't deleted from DB: " + address, ex);
            throw new AddressServiceException("Tenant address wasn't deleted");
        }

        logger.info("Tenant address was deleted from DB: tenant.guid = {}, address = {}", tenantGuid, address);
    }

    @Transactional(readOnly = true)
    @Override
    public IAddress getAddressForTenant(UUID addressGuid, UUID tenantGuid) {
        logger.info("Get Tenant address from DB: tenant.guid = {}, address.guid = {}", tenantGuid, addressGuid);

        Address address = findAddressByGuid(addressGuid);
        Tenant tenant = findTenantByGuid(tenantGuid);

        checkAddressBelongsTenant(address, tenant);

        return address;
    }

    @Transactional(readOnly = true)
    @Override
    public List<IAddress> getAddressAllForTenant(UUID tenantGuid) {
        logger.info("Get Tenant address list: tenant.guid = {}", tenantGuid);

        List<? extends IAddress> addresses = addressRepository.findAllByTenantGuid(tenantGuid);

        return (List<IAddress>) addresses;
    }

    @Transactional
    @Override
    public IAddress createAddressForUser(IAddress addressData, UUID userGuid) {
        logger.info("Insert User address into DB: user.guid = {}, address = {}", userGuid, addressData);

        Address address = new Address();
        User user = findUserByGuid(userGuid);

        address.setGuid(UUID.randomUUID());
        address.setAddress(addressData.getAddress());
        address.setAlias(addressData.getAlias());
        address.setNotes(addressData.getNotes());
        address.setUser(user);

        try {
            address = addressRepository.save(address);
        } catch (PersistenceException | ConstraintViolationException ex) {
            logger.error("User address wasn't inserted into DB: " + address, ex);
            throw new UserServiceException("User address wasn't inserted");
        }

        logger.info("User address was inserted into DB: user.guid = {}, address = {}", userGuid, address);

        return address;
    }

    @Transactional
    @Override
    public IAddress updateAddressForUser(IAddress addressData, UUID userGuid) {
        logger.info("Update User address in DB: user.guid = {}, address = {}", userGuid, addressData);

        Address address = findAddressByGuid(addressData.getGuid());
        User user = findUserByGuid(userGuid);

        checkAddressBelongsUser(address, user);

        address.setAddress(addressData.getAddress());
        address.setAlias(addressData.getAlias());
        address.setNotes(addressData.getNotes());

        try {
            address = addressRepository.save(address);
        } catch (PersistenceException | ConstraintViolationException ex) {
            logger.error("User address wasn't updated in DB: " + address, ex);
            throw new UserServiceException("User address wasn't updated");
        }

        logger.info("User address was updated in DB: user.guid = {}, address = {}", userGuid, address);

        return address;
    }

    @Transactional
    @Override
    public void deleteAddressForUser(UUID addressGuid, UUID userGuid) {
        logger.info("Delete User address from DB: user.guid = {}, address.guid = {}", userGuid, addressGuid);

        Address address = findAddressByGuid(addressGuid);
        User user = findUserByGuid(userGuid);

        checkAddressBelongsUser(address, user);

        try {
            addressRepository.deleteById(address.getIdAddress());
        } catch (PersistenceException ex) {
            logger.error("User address wasn't deleted from DB: " + address, ex);
            throw new AddressServiceException("User address wasn't deleted");
        }

        logger.info("User address was deleted from DB: user.guid = {}, address = {}", userGuid, address);
    }

    @Transactional(readOnly = true)
    @Override
    public IAddress getAddressForUser(UUID addressGuid, UUID userGuid) {
        logger.info("Get User address from DB: user.guid = {}, address.guid = {}", userGuid, addressGuid);

        Address address = findAddressByGuid(addressGuid);
        User user = findUserByGuid(userGuid);

        checkAddressBelongsUser(address, user);

        return address;
    }

    @Transactional(readOnly = true)
    @Override
    public List<IAddress> getAddressAllForUser(UUID userGuid) {
        logger.info("Get User address list: user.guid = {}", userGuid);

        List<? extends IAddress> addresses = addressRepository.findAllByUserGuid(userGuid);

        return (List<IAddress>) addresses;
    }

    private Address findAddressByGuid(UUID guid) {
        logger.info("Find Address in DB: guid = {}", guid);

        Address address = addressRepository.findByGuid(guid);
        if (address == null) {
            logger.error("Address wasn't found in DB: guid = {}", guid);
            throw new AddressServiceException("Address wasn't found");
        }
        return address;
    }

    private Tenant findTenantByGuid(UUID guid) {
        logger.info("Address Service, find Tenant in DB: guid = {}", guid);

        Tenant tenant = tenantRepository.findByGuid(guid);
        if (tenant == null) {
            logger.error("Address Service, Tenant wasn't found in DB: guid = {}", guid);
            throw new AddressServiceException("Tenant wasn't found");
        }
        return tenant;
    }

    private User findUserByGuid(UUID guid) {
        logger.info("Address Service, find User in DB: guid = {}", guid);

        User user = userRepository.findByGuid(guid);
        if (user == null) {
            logger.error("Address Service, User wasn't found in DB: guid = {}", guid);
            throw new AddressServiceException("User wasn't found");
        }
        return user;
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
