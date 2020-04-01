package com.softserve.itacademy.kek.services.impl;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    private final static Logger logger = LoggerFactory.getLogger(ITenantService.class);

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
        logger.info("Insert Tenant into DB: {}", iTenant);

        final UUID ownerGuid = iTenant.getTenantOwner().getGuid();

        final Tenant tenantForSaving = transform(iTenant);
        tenantForSaving.setGuid(UUID.randomUUID());

        try {
            final User tenantOwner = userRepository.findByGuid(ownerGuid).orElse(null);

            if (tenantOwner == null) {
                Exception noSuchElementException = new NoSuchElementException();
                logger.error("User for Tenant was not found in DB: user.guid = " + ownerGuid, noSuchElementException);
                throw new TenantServiceException("User for Tenant was not found", noSuchElementException);
            }

            tenantOwner.setTenant(tenantForSaving);
            tenantForSaving.setTenantOwner(tenantOwner);

            final Tenant insertedTenant = tenantRepository.saveAndFlush(tenantForSaving);

            logger.debug("Tenant was inserted into DB: {}", insertedTenant);

            return insertedTenant;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while inserting Tenant into DB: " + tenantForSaving, ex);
            throw new TenantServiceException("An error occurred while inserting tenant", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ITenant> getAll() {
        logger.info("Get all Tenants from DB");

        try {
            final List<? extends ITenant> tenants = tenantRepository.findAll();
            return (List<ITenant>) tenants;
        } catch (DataAccessException ex) {
            logger.error("Error while getting all Tenants from DB", ex);
            throw new TenantServiceException("An error occurs while getting tenants", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ITenant getByGuid(UUID guid) throws TenantServiceException {
        logger.info("Get Tenant from DB by guid: {}", guid);

        try {
            final Tenant tenant = tenantRepository.findByGuid(guid).orElseThrow(() -> {
                logger.error("Tenant wasn't found in the database");
                return new TenantServiceException("Tenant was not found in database for guid: " + guid, new NoSuchElementException());
            });
            return tenant;

        } catch (DataAccessException ex) {
            logger.error("Error while getting Tenant from DB by guid: " + guid, ex);
            throw new TenantServiceException("An error occurs while getting tenant", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ITenant> getAllPageable(Pageable pageable) {
        logger.info("Get a page of Tenants from DB: {}", pageable);

        try {
            final Page<? extends ITenant> tenants = tenantRepository.findAll(pageable);
            return (Page<ITenant>) tenants;
        } catch (DataAccessException ex) {
            logger.error("Error while getting a page of Tenants from DB: " + pageable, ex);
            throw new TenantServiceException("An error occurred while getting tenants", ex);
        }
    }

    @Transactional
    @Override
    public ITenant update(ITenant iTenant, UUID guid) throws TenantServiceException {
        logger.info("Update Tenant in DB by guid: {}", guid);

        final Tenant tenantForUpdating = (Tenant) getByGuid(guid);

        final TenantDetails tenantDetails = (TenantDetails) tenantForUpdating.getTenantDetails();
        tenantDetails.setPayload(iTenant.getTenantDetails().getPayload());
        tenantDetails.setImageUrl(iTenant.getTenantDetails().getImageUrl());

        tenantForUpdating.setTenantDetails(tenantDetails);
        tenantForUpdating.setName(iTenant.getName());

        try {
            final Tenant updatedTenant = tenantRepository.saveAndFlush(tenantForUpdating);

            logger.debug("Tenant was updated in DB: {}", updatedTenant);

            return updatedTenant;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while updating Tenant in DB: " + tenantForUpdating, ex);
            throw new TenantServiceException("An error occurred while updating tenant", ex);
        }
    }

    @Transactional
    @Override
    public void deleteByGuid(UUID guid) throws TenantServiceException {
        logger.info("Delete Tenant by guid: {}", guid);

        final Tenant tenant = (Tenant) getByGuid(guid);

        try {
            tenantRepository.delete(tenant);
            tenantRepository.flush();

            logger.debug("Tenant was deleted from DB: {}", tenant);
        } catch (DataAccessException ex) {
            logger.error("Error while deleting Tenant from DB: " + tenant, ex);
            throw new TenantServiceException("An error occurred while deleting tenant", ex);
        }
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
