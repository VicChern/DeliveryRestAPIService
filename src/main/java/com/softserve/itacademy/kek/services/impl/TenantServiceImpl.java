package com.softserve.itacademy.kek.services.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.exception.TenantServiceException;
import com.softserve.itacademy.kek.mappers.ITenantDetailsMapper;
import com.softserve.itacademy.kek.mappers.ITenantMapper;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.ITenantDetails;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.TenantDetails;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.ITenantService;

/**
 * Service implementation for {@link ITenantService}
 */
@Service
public class TenantServiceImpl implements ITenantService {

    private final static Logger logger = LoggerFactory.getLogger(TenantServiceImpl.class);

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    @Autowired
    public TenantServiceImpl(TenantRepository tenantRepository,
                             UserRepository userRepository) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public ITenant create(ITenant tenant) throws TenantServiceException {
        logger.info("Insert tenant into DB: {}", tenant);

        try {
            final Tenant tenantForSaving = ITenantMapper.INSTANCE.toTenant(tenant);
            tenantForSaving.setGuid(UUID.randomUUID());

            TenantDetails actualDetails = new TenantDetails();
            ITenantDetails details = tenant.getTenantDetails();

            if (details != null) actualDetails = ITenantDetailsMapper.INSTANCE.toTenantDetails(details);

            tenantForSaving.setTenantDetails(actualDetails);

            final UUID ownerGuid = tenant.getTenantOwner().getGuid();
            final User owner = userRepository.findByGuid(ownerGuid).orElseThrow(
                    () -> new NoSuchElementException("User was not found"));

            owner.setTenant(tenantForSaving);
            tenantForSaving.setTenantOwner(owner);

            final Tenant insertedTenant = tenantRepository.saveAndFlush(tenantForSaving);

            logger.debug("Tenant was inserted into DB: {}", insertedTenant);

            return insertedTenant;
        } catch (Exception ex) {
            logger.error("Error while inserting Tenant into DB: " + tenant, ex);
            throw new TenantServiceException("An error occurred while inserting tenant", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ITenant> getAll() throws TenantServiceException {
        logger.info("Get all tenants from DB");

        try {
            final List<? extends ITenant> tenants = tenantRepository.findAll();

            logger.debug("All tenants were gotten from DB");

            return (List<ITenant>) tenants;
        } catch (Exception ex) {
            logger.error("Error while getting all tenants from DB", ex);
            throw new TenantServiceException("An error occurs while getting tenants", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ITenant getByGuid(UUID guid) throws TenantServiceException {
        logger.info("Get tenant from DB by guid: {}", guid);

        try {
            final Tenant tenant = tenantRepository.findByGuid(guid).orElseThrow(
                    () -> new NoSuchElementException("Tenant was not found"));

            logger.debug("Tenant was gotten from DB: {}", tenant);

            return tenant;
        } catch (Exception ex) {
            logger.error("Error while getting tenant from DB by guid: " + guid, ex);
            throw new TenantServiceException("An error occurs while getting tenant", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ITenant> getAllPageable(Pageable pageable) throws TenantServiceException {
        logger.info("Get a page of tenants from DB: {}", pageable);

        try {
            final Page<? extends ITenant> tenants = tenantRepository.findAll(pageable);

            logger.debug("A page of tenants was gotten from DB");

            return (Page<ITenant>) tenants;
        } catch (Exception ex) {
            logger.error("Error while getting a page of Tenants from DB: " + pageable, ex);
            throw new TenantServiceException("An error occurred while getting tenants", ex);
        }
    }

    @Transactional
    @Override
    public ITenant update(ITenant tenant, UUID guid) throws TenantServiceException {
        logger.info("Update tenant in DB by guid: {}", guid);

        final Tenant tenantForUpdating = (Tenant) getByGuid(guid);

        try {
            final TenantDetails tenantDetails = (TenantDetails) tenantForUpdating.getTenantDetails();
            tenantDetails.setPayload(tenant.getTenantDetails().getPayload());
            tenantDetails.setImageUrl(tenant.getTenantDetails().getImageUrl());

            tenantForUpdating.setTenantDetails(tenantDetails);
            tenantForUpdating.setName(tenant.getName());

            final Tenant updatedTenant = tenantRepository.saveAndFlush(tenantForUpdating);

            logger.debug("Tenant was updated in DB: {}", updatedTenant);

            return updatedTenant;
        } catch (Exception ex) {
            logger.error("Error while updating Tenant in DB: " + tenantForUpdating, ex);
            throw new TenantServiceException("An error occurred while updating tenant", ex);
        }
    }

    @Transactional
    @Override
    public void deleteByGuid(UUID guid) throws TenantServiceException {
        logger.info("Delete tenant by guid: {}", guid);

        final Tenant tenant = (Tenant) getByGuid(guid);

        try {
            tenantRepository.deleteById(tenant.getIdTenant());
            tenantRepository.flush();

            logger.debug("Tenant was deleted from DB: {}", tenant);
        } catch (Exception ex) {
            logger.error("Error while deleting tenant from DB: " + tenant, ex);
            throw new TenantServiceException("An error occurred while deleting tenant", ex);
        }
    }

    @Transactional
    @Override
    public void deleteAll() throws TenantServiceException {
        logger.info("Delete all tenants");

        try {
            tenantRepository.deleteAll();

            logger.debug("All tenants was deleted from DB");
        } catch (Exception ex) {
            logger.error("Error while deleting tenants from DB: ", ex);
            throw new OrderServiceException("An error occurred while deleting tenants", ex);
        }
    }
}
