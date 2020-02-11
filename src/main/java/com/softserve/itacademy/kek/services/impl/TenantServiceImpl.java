package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.converter.objectConverter.TenantObjectConverter;
import com.softserve.itacademy.kek.exception.TenantServiceException;
import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.models.User;
import com.softserve.itacademy.kek.objects.TenantObject;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.ITenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.UUID;

/**
 * Service implementation for {@link ITenantService}
 */
@Service
public class TenantServiceImpl implements ITenantService {

    final Logger logger = LoggerFactory.getLogger(ITenantService.class);

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final TenantObjectConverter tenantObjectConverter;

    @Autowired
    public TenantServiceImpl(TenantRepository tenantRepository, UserRepository userRepository, TenantObjectConverter tenantObjectConverter) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.tenantObjectConverter = tenantObjectConverter;
    }


    @Override
    public TenantObject save(TenantObject tenantObject) throws TenantServiceException {
        logger.info("Save Tenant to db: " + tenantObject);
        User tenantOwner;

        // get user for tenant
        try {
          tenantOwner = userRepository.findByGuid(UUID.fromString(tenantObject.getOwner()));
        } catch (EntityNotFoundException ex) {
            logger.error("There is no User in db for Tenant with user guid: " + tenantObject.getOwner());
            throw new TenantServiceException("There is no User for Tenant with user guid: " + tenantObject.getOwner());
        }

        Tenant tenant = tenantObjectConverter.transform(tenantObject, tenantOwner);

        // save tenant to db
        try {
            tenantRepository.save(tenant);
        } catch (PersistenceException ex) {
            logger.error("Tenant wasn't saved: " + tenant);
            throw new TenantServiceException("Tenant wasn't saved");
        }

        logger.info("Tenant was saved: " + tenant);
        return tenantObjectConverter.transform(tenant);
    }

    @Override
    public TenantObject get() {
        throw new TenantServiceException("Method get() will be implemented when will be added security.");
    }

    @Override
    public TenantObject getByGuid(UUID guid) {
        throw new TenantServiceException("Method getByGuid(UUID guid) isn't implemented yet");
    }

    @Override
    public TenantObject update(TenantObject tenant, UUID guid) {
        throw new TenantServiceException("Method update(TenantObject tenant, UUID guid) isn't implemented yet");
    }

    @Override
    public void deleteByGuid(UUID guid) {
        throw new TenantServiceException("Method deleteByGuid(UUID guid) isn't implemented yet");
    }

}
