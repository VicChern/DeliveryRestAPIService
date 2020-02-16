package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.modelInterfaces.IAddress;
import com.softserve.itacademy.kek.modelInterfaces.ITenant;
import com.softserve.itacademy.kek.models.Address;
import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.models.TenantDetails;
import com.softserve.itacademy.kek.repositories.AddressRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IAddressService;
import com.softserve.itacademy.kek.services.ITenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service implementation for {@link IAddressService}
 */
@Service
public class AddressServiceImpl implements IAddressService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ITenantService.class);

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

    @Transactional(readOnly = true)
    @Override
    public List<IAddress> getAddressesByTenantGuid(UUID tenantGuid) {
        return null;
    }

    @Transactional
    @Override
    public IAddress createAddressForTenant(IAddress address, UUID tenantGuid) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public IAddress getAddressForTenantByGuid(UUID tenantGuid, UUID addressGuid) {
        return null;
    }

    @Transactional
    @Override
    public IAddress updateAddressForTenantByGuid(UUID tenantGuid, UUID addressGuid, IAddress address) {
        return null;
    }

    @Transactional
    @Override
    public void deleteAddressForTenantByGuid(UUID tenantGuid, UUID addressGuid) {

    }

    @Transactional(readOnly = true)
    @Override
    public List<IAddress> getAddressesByUserGuid(UUID userGuid) {
        return null;
    }

    @Transactional
    @Override
    public IAddress createAddressForUser(IAddress address, UUID userGuid) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public IAddress getAddressForUserByGuid(UUID userGuid, UUID addressGuid) {
        return null;
    }

    @Transactional
    @Override
    public IAddress updateAddressForUserByGuid(UUID userGuid, UUID addressGuid, IAddress address) {
        return null;
    }

    @Transactional
    @Override
    public void deleteAddressForUserByGuid(UUID userGuid, UUID addressGuid) {

    }

    private Address transform(IAddress iAddress) {

        Address address = new Address();
        address.setAddress(iAddress.getAddress());



        return address;
    }
}
