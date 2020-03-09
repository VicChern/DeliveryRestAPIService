package com.softserve.itacademy.kek.services.impl;

import javax.persistence.PersistenceException;
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
        LOGGER.debug("Save Tenant to db: {}", iTenant);

        final UUID ownerGuid = iTenant.getTenantOwner().getGuid();

        final Tenant tenantForSaving = transform(iTenant);
        tenantForSaving.setGuid(UUID.randomUUID());

        // check if exist user for tenant
        //TODO replace by checking whether the ownerGuid is guid of principal user (when will be added security)

        final User tenantOwner = userRepository.findByGuid(ownerGuid).orElse(null);

        if (tenantOwner == null) {
            LOGGER.error("There is no User in db for Tenant with user guid: {}", ownerGuid);
            throw new TenantServiceException("There is no User for Tenant with user guid: " + ownerGuid, null);
        }

        tenantOwner.setTenant(tenantForSaving);
        tenantForSaving.setTenantOwner(tenantOwner);

        // save tenant to db
        try {
            return tenantRepository.save(tenantForSaving);
        } catch (PersistenceException ex) {
            LOGGER.error("Tenant wasn't saved: {}", tenantForSaving);
            throw new TenantServiceException("Tenant wasn't saved: " + iTenant, ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ITenant> getAll() {
        LOGGER.debug("Getting all Tenants from db");
        List<? extends ITenant> tenants = tenantRepository.findAll();

        return (List<ITenant>) tenants;
    }

    @Transactional(readOnly = true)
    @Override
    public ITenant getByGuid(UUID guid) throws TenantServiceException {
        LOGGER.debug("Get Tenant by guid from db: {}", guid);

        try {
            return tenantRepository.findByGuid(guid).orElseThrow();
        } catch (NoSuchElementException ex) {
            LOGGER.error("There is no Tenant in db for guid: {}", guid);
            throw new TenantServiceException("Tenant wasn't found for guid: " + guid, ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ITenant> getAllPageable(Pageable pageable) {
        LOGGER.debug("Getting pageable: {} Tenants from db", pageable);

        Page<? extends ITenant> tenants = tenantRepository.findAll(pageable);

        return (Page<ITenant>) tenants;
    }

    @Transactional
    @Override
    public ITenant update(ITenant iTenant, UUID guid) throws TenantServiceException {
        LOGGER.debug("Update Tenant by guid: {}", guid);

        final Tenant tenantForUpdating = (Tenant) getByGuid(guid);

        final TenantDetails tenantDetails = (TenantDetails) tenantForUpdating.getTenantDetails();
        tenantDetails.setPayload(iTenant.getTenantDetails().getPayload());
        tenantDetails.setImageUrl(iTenant.getTenantDetails().getImageUrl());

        tenantForUpdating.setTenantDetails(tenantDetails);
        tenantForUpdating.setName(iTenant.getName());

        try {
            return tenantRepository.save(tenantForUpdating);
        } catch (PersistenceException ex) {
            LOGGER.error("Tenant wasn't updated for guid: {}", guid);
            throw new TenantServiceException("Tenant wasn't updated for guid: " + guid, ex);
        }
    }

    @Transactional
    @Override
    public void deleteByGuid(UUID guid) throws TenantServiceException {
        LOGGER.debug("Delete Tenant by guid: {}", guid);

        final Tenant tenant = (Tenant) getByGuid(guid);
        tenantRepository.delete(tenant);

        LOGGER.debug("Tenant was deleted for guid: {}", guid);
    }

    /**
     * Transform {@link ITenant} to {@link Tenant}
     *
     * @param iTenant iTenant
     * @return transformed tenant
     */
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
