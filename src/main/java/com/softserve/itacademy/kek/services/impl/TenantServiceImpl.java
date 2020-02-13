package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.exception.TenantServiceException;
import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.modelInterfaces.ITenant;
import com.softserve.itacademy.kek.models.TenantDetails;
import com.softserve.itacademy.kek.models.User;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.ITenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.UUID;

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
    public ITenant save(ITenant tenant) throws TenantServiceException {
        LOGGER.info("Save Tenant to db: {}", tenant);
        User tenantOwner;
        UUID ownerGuid = tenant.getTenantOwner().getGuid();
        Tenant tenantForSaving = transform(tenant);

        tenantForSaving.setGuid(UUID.randomUUID());

        // check if exist user for tenant
        //TODO replace by checking whether the ownerGuid is guid of principal user (when will be added security)
        try {
         tenantOwner = userRepository.findByGuid(ownerGuid);
         tenantOwner.setTenant(tenantForSaving);
         tenantForSaving.setTenantOwner(tenantOwner);
        } catch (EntityNotFoundException ex) {
            LOGGER.error("There is no User in db for Tenant with user guid: {}", ownerGuid);
            throw new TenantServiceException("There is no User for Tenant with user guid: " + ownerGuid);
        }

        // save tenant to db
        try {
            tenantRepository.save(tenantForSaving);
        } catch (PersistenceException ex) {
            LOGGER.error("Tenant wasn't saved: {}", tenantForSaving);
            throw new TenantServiceException("Tenant wasn't saved");
        }

        LOGGER.info("Tenant was saved: " + tenantForSaving);
        return tenant;
    }


    @Transactional(readOnly = true)
    @Override
    public ITenant get() {
        throw new TenantServiceException("Method get() will be implemented when will be added security.");
    }

    @Transactional(readOnly = true)
    @Override
    public ITenant getByGuid(UUID guid) {
        LOGGER.info("Get Tenant by guid from db: {}", guid);
        ITenant tenant;

        try {
            tenant = tenantRepository.findByGuid(guid);
        } catch (EntityNotFoundException ex) {
            LOGGER.error("There is no Tenant in db for guid: {}", guid);
            throw new TenantServiceException("Tenant wasn't found for guid: " + guid);
        }

        LOGGER.info("Tenant was found: " + tenant);
        return tenant;
    }

    @Transactional
    @Override
    public ITenant update(ITenant tenant, UUID guid) {
        LOGGER.info("Update Tenant by guid: {}", guid);
        Tenant tenantForUpdating;

        try {
             tenantForUpdating = tenantRepository.findByGuid(guid);

            tenantForUpdating.setName(tenant.getName());
            tenantForUpdating.getTenantDetails().setPayload(tenant.getTenantDetails().getPayload());
            tenantForUpdating.getTenantDetails().setImageUrl(tenant.getTenantDetails().getImageUrl());

        } catch (EntityNotFoundException ex) {
            LOGGER.error("There is no Tenant in db for guid: {}", guid);
            throw new TenantServiceException("Tenant wasn't found for guid: " + guid);
        }

        try {
            tenantRepository.save(tenantForUpdating);
        } catch (PersistenceException ex) {
            LOGGER.error("Tenant wasn't updated for guid: {}", guid);
            throw new TenantServiceException("Tenant wasn't updated for guid: " + guid);
        }

        LOGGER.info("Tenant was updated: {}", tenantForUpdating);
        return tenantForUpdating;
    }

    @Transactional
    @Override
    public void deleteByGuid(UUID guid) {
        LOGGER.info("Delete Tenant by guid: {}", guid);

        try {
            tenantRepository.removeByGuid(guid);
        } catch (PersistenceException ex) {
            LOGGER.error("Tenant wasn't deleted for guid: {}", guid);
            throw new TenantServiceException("Tenant wasn't deleted for guid: " + guid);
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
