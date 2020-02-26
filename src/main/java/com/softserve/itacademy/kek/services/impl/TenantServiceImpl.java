package com.softserve.itacademy.kek.services.impl;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.TenantServiceException;
import com.softserve.itacademy.kek.models.ITenant;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(ITenantService.class);

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
    public ITenant create(ITenant iTenant) throws TenantServiceException {
        LOGGER.info("Save Tenant to db: {}", iTenant);

        final UUID ownerGuid = iTenant.getTenantOwner().getGuid();

        final Tenant tenantForSaving = transform(iTenant);
        tenantForSaving.setGuid(UUID.randomUUID());

        // check if exist user for tenant
        //TODO replace by checking whether the ownerGuid is guid of principal user (when will be added security)
        try {
            final User tenantOwner = userRepository.findByGuid(ownerGuid);

            tenantOwner.setTenant(tenantForSaving);
            tenantForSaving.setTenantOwner(tenantOwner);

        } catch (EntityNotFoundException ex) {
            LOGGER.error("There is no User in db for Tenant with user guid: {}", ownerGuid);
            throw new TenantServiceException(ex, "There is no User for Tenant with user guid: " + ownerGuid);
        }

        // save tenant to db
        try {
             return tenantRepository.save(tenantForSaving);
        } catch (PersistenceException ex) {
            LOGGER.error("Tenant wasn't saved: {}", tenantForSaving);
            throw new TenantServiceException(ex, "Tenant wasn't saved: " + iTenant);
        }
    }


    @Transactional(readOnly = true)
    @Override
    public List<ITenant> getAll() {

        LOGGER.info("Getting all Tenants from db");
        List<ITenant> tenants = new ArrayList<>();

        tenantRepository.findAll().forEach(tenants::add);
        if (tenants.isEmpty()) {
            LOGGER.error("No one tenant was found.");
            throw new TenantServiceException("No one tenant was found.");
        }

        LOGGER.info("Tenants was found: {}", tenants);
        return tenants;
    }

    @Transactional(readOnly = true)
    @Override
    public ITenant getByGuid(UUID guid) {
        LOGGER.info("Get Tenant by guid from db: {}", guid);

        try {
            return tenantRepository.findByGuid(guid).orElseThrow();
        } catch (NoSuchElementException ex) {
            LOGGER.error("There is no Tenant in db for guid: {}", guid);
            throw new TenantServiceException("Tenant wasn't found for guid: " + guid);
        }
    }

    @Transactional
    @Override
    public ITenant update(ITenant iTenant, UUID guid) {
        LOGGER.info("Update Tenant by guid: {}", guid);

        Tenant tenantForUpdating = (Tenant) getByGuid(guid);

        TenantDetails tenantDetails = (TenantDetails) tenantForUpdating.getTenantDetails();
        tenantDetails.setPayload(iTenant.getTenantDetails().getPayload());
        tenantDetails.setImageUrl(iTenant.getTenantDetails().getImageUrl());

        tenantForUpdating.setTenantDetails(tenantDetails);
        tenantForUpdating.setName(iTenant.getName());

        try {
           return tenantRepository.save(tenantForUpdating);
        } catch (PersistenceException ex) {
            LOGGER.error("Tenant wasn't updated for guid: {}", guid);
            throw new TenantServiceException(ex, "Tenant wasn't updated for guid: " + guid);
        }
    }

    @Transactional
    @Override
    public void deleteByGuid(UUID guid) {
        LOGGER.info("Delete Tenant by guid: {}", guid);

        try {
            tenantRepository.removeByGuid(guid);
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error("Tenant wasn't deleted for guid: {}", guid);
            throw new TenantServiceException(ex, "Tenant wasn't deleted for guid: " + guid);
        }

        LOGGER.info("Tenant was deleted for guid: {}", guid);
    }


    private Tenant transform(ITenant iTenant) {
        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setPayload(iTenant.getTenantDetails().getPayload());
        tenantDetails.setImageUrl(iTenant.getTenantDetails().getImageUrl());

        Tenant tenant = new Tenant();
        tenant.setName(iTenant.getName());

        tenant.setTenantDetails(tenantDetails);
        tenantDetails.setTenant(tenant);

        return tenant;
    }
}
