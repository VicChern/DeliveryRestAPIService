package com.softserve.itacademy.kek.services.impl;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.TenantPropertiesServiceException;
import com.softserve.itacademy.kek.exception.TenantServiceException;
import com.softserve.itacademy.kek.exception.UserServiceException;
import com.softserve.itacademy.kek.models.ITenantProperties;
import com.softserve.itacademy.kek.models.impl.PropertyType;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.TenantProperties;
import com.softserve.itacademy.kek.repositories.TenantPropertiesRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.services.IPropertyTypeService;
import com.softserve.itacademy.kek.services.ITenantPropertiesService;
import com.softserve.itacademy.kek.services.ITenantService;

/**
 * Service implementation for {@link ITenantPropertiesService}
 */
@Service
public class TenantPropertiesServiceImpl implements ITenantPropertiesService {

    private final static Logger logger = LoggerFactory.getLogger(ITenantPropertiesService.class);

    private final TenantPropertiesRepository tenantPropertiesRepository;
    private final TenantRepository tenantRepository;
    private final ITenantService tenantService;
    private final IPropertyTypeService propertyTypeService;

    @Autowired
    public TenantPropertiesServiceImpl(TenantPropertiesRepository tenantPropertiesRepository,
                                       TenantRepository tenantRepository,
                                       ITenantService tenantService,
                                       IPropertyTypeService propertyTypeService) {
        this.tenantPropertiesRepository = tenantPropertiesRepository;
        this.propertyTypeService = propertyTypeService;
        this.tenantRepository = tenantRepository;
        this.tenantService = tenantService;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ITenantProperties> getAllForTenant(UUID tenantGuid) {
        logger.info("Get a list of Tenant Properties from DB: Tenant.guid = {}", tenantGuid);

        try {
            final List<? extends ITenantProperties> tenantProperties = tenantPropertiesRepository.findAll();
            return (List<ITenantProperties>) tenantProperties;
        } catch (DataAccessException ex) {
            logger.error("Error while getting a list of Tenant Properties from DB: Tenant.guid = " + tenantGuid, ex);
            throw new TenantServiceException("An error occurs while getting tenant properties", ex);
        }
    }

    @Transactional
    @Override
    public List<ITenantProperties> create(List<ITenantProperties> iTenantProperties, UUID tenantGuid) throws TenantPropertiesServiceException {
        logger.info("Insert a list of Tenant Properties into DB: Tenant.guid = {}, TenantProperties = {}",
                tenantGuid, iTenantProperties);

        final Tenant tenant = (Tenant) tenantService.getByGuid(tenantGuid);

        final List<TenantProperties> tenantProperties = new ArrayList<>();
        for (ITenantProperties p : iTenantProperties) {
            TenantProperties tenantProperty = transform(p);
            tenantProperty.setGuid(UUID.randomUUID());
            tenantProperties.add(tenantProperty);
        }

        tenantProperties.forEach(tenant::addTenantProperty);

        final Tenant updatedTenant;
        try {
            updatedTenant = tenantRepository.saveAndFlush(tenant);

            logger.debug("A list of Tenant Properties was inserted into DB: Tenant = {}, TenantProperties = {}",
                    tenant, tenantProperties);
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error(String.format("Error while inserting a list of Tenant Properties into DB: Tenant = %s, TenantProperties = %s",
                    tenant, tenantProperties), ex);
            throw new TenantPropertiesServiceException("An error occurs while inserting tenant properties", ex);
        }

        // filter out only those tenant properties, that were saved
        Set<String> keys = iTenantProperties
                .stream()
                .map(ITenantProperties::getKey)
                .collect(Collectors.toSet());

        return updatedTenant.getTenantPropertiesList()
                .stream()
                .filter(tenantProperty -> keys.contains(tenantProperty.getKey()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ITenantProperties getPropertyByTenantGuid(UUID tenantGuid, UUID tenantPropertyGuid) throws TenantPropertiesServiceException {
        logger.debug("Getting TenantProperties by tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);

        try {
            final TenantProperties tenantProperties = tenantPropertiesRepository
                    .findByGuidAndTenantGuid(tenantPropertyGuid, tenantGuid)
                    .orElseThrow(() -> {
                        logger.error("No one tenantProperties was found by tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);
                        return new TenantPropertiesServiceException("Tenant properties was not found in database for guid: " + tenantGuid, new NoSuchElementException());
                    });
            return tenantProperties;

        } catch (DataAccessException ex) {
            logger.error(String.format("Error while getting Tenant Property from DB: Tenant.guid = %s, TenantProperties.guid = %s",
                    tenantGuid, tenantPropertyGuid), ex);
            throw new TenantPropertiesServiceException("An error occurs while getting tenant property", ex);
        }
    }

    @Transactional
    @Override
    public ITenantProperties update(UUID tenantGuid, UUID tenantPropertyGuid, ITenantProperties iTenantProperty) throws TenantPropertiesServiceException {
        logger.info("Update Tenant Property in DB: Tenant.guid = {}, TenantProperties.guid = {}, new TenantProperties = {}",
                tenantGuid, tenantPropertyGuid, iTenantProperty);

        // find tenant property for updating
        final TenantProperties tenantProperty = (TenantProperties) getPropertyByTenantGuid(tenantGuid, tenantPropertyGuid);

        final PropertyType propertyType = (PropertyType) propertyTypeService.produce(iTenantProperty.getPropertyType());
        tenantProperty.setPropertyType(propertyType);

        if (iTenantProperty.getKey() != null) {
            tenantProperty.setKey(iTenantProperty.getKey());
        }
        if (iTenantProperty.getValue() != null) {
            tenantProperty.setValue(iTenantProperty.getValue());
        }

        // save updated tenant property
        try {
            final TenantProperties updatedProperties = tenantPropertiesRepository.saveAndFlush(tenantProperty);

            logger.debug("Tenant Property was updated in DB: Tenant.guid = {},  TenantProperties = {}",
                    tenantGuid, updatedProperties);

            return updatedProperties;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error(String.format(
                    "Error while updating Tenant Property in DB: Tenant.guid = %s, TenantProperties.guid = %s, new TenantProperties = %s",
                    tenantGuid, tenantPropertyGuid, iTenantProperty), ex);
            throw new TenantPropertiesServiceException("An error occurs while updating Tenant Properties", ex);
        }
    }

    @Transactional
    @Override
    public void delete(UUID tenantGuid, UUID tenantPropertyGuid) throws TenantPropertiesServiceException {
        logger.info("Delete Tenant Property from DB: Tenant.guid = {}, TenantProperties.guid = {}",
                tenantGuid, tenantPropertyGuid);

        final TenantProperties tenantProperties = (TenantProperties) getPropertyByTenantGuid(tenantGuid, tenantPropertyGuid);
        try {
            tenantPropertiesRepository.delete(tenantProperties);
            tenantPropertiesRepository.flush();

            logger.debug("Tenant Property was deleted from DB: Tenant.guid = {},  TenantProperties = {}",
                    tenantGuid, tenantProperties);
        } catch (DataAccessException ex) {
            logger.error(String.format("Error while deleting Tenant Property from DB: Tenant.guid = %s, TenantProperties = %s",
                    tenantGuid, tenantProperties), ex);
            throw new UserServiceException("An error occurred while deleting tenant property", ex);
        }
    }

    /**
     * Transform {@link ITenantProperties} to {@link TenantProperties}
     *
     * @param iTenantProperties iTenantProperties
     * @return transformed tenant properties
     */
    private TenantProperties transform(ITenantProperties iTenantProperties) {

        final TenantProperties tenantProperties = new TenantProperties();
        tenantProperties.setGuid(iTenantProperties.getGuid());
        tenantProperties.setKey(iTenantProperties.getKey());
        tenantProperties.setValue(iTenantProperties.getValue());

        final PropertyType propertyType = (PropertyType) propertyTypeService.produce(iTenantProperties.getPropertyType());
        tenantProperties.setPropertyType(propertyType);
//        tenantProperties.setTenant(iTenantProperties.getTenant());

        return tenantProperties;
    }
}
