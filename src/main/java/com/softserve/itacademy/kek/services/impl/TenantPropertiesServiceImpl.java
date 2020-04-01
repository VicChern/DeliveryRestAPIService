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

    private final static Logger LOGGER = LoggerFactory.getLogger(ITenantPropertiesService.class);

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
        LOGGER.debug("Getting all TenantProperties for tenant guid: {}", tenantGuid);

        List<? extends ITenantProperties> tenantProperties = tenantPropertiesRepository.findAll();

        return (List<ITenantProperties>) tenantProperties;
    }

    @Transactional
    @Override
    public List<ITenantProperties> create(List<ITenantProperties> iTenantProperties, UUID tenantGuid) throws TenantPropertiesServiceException {
        LOGGER.debug("Save tenant properties for tenant guid {} to db: {}", tenantGuid, iTenantProperties);

        final Tenant tenant = (Tenant) tenantService.getByGuid(tenantGuid);

        List<TenantProperties> tenantProperties = new ArrayList<>();
        for (ITenantProperties p : iTenantProperties) {
            TenantProperties tenantProperty = transform(p);
            tenantProperty.setGuid(UUID.randomUUID());
            tenantProperties.add(tenantProperty);
        }

        tenantProperties.forEach(tenant::addTenantProperty);

        final Tenant updatedTenant;
        try {
            updatedTenant = tenantRepository.save(tenant);
        } catch (ConstraintViolationException | DataAccessException ex) {
            LOGGER.error("Tenant properties wasn't saved for tenant guid: {}, properties: {}", tenantGuid, iTenantProperties);
            throw new TenantPropertiesServiceException("Tenant properties wasn't saved for tenant guid: " + tenantGuid, ex);
        }

        LOGGER.debug("Tenant properties was saved for tenant guid: {}, properties: {}", tenantGuid, iTenantProperties);

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
    public ITenantProperties get(UUID tenantGuid, UUID tenantPropertyGuid) throws TenantPropertiesServiceException {
        LOGGER.debug("Getting TenantProperties by tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);

        try {
            return tenantPropertiesRepository
                    .findByGuidAndTenantGuid(tenantPropertyGuid, tenantGuid)
                    .orElseThrow();
        } catch (NoSuchElementException ex) {
            LOGGER.error("No one tenantProperties was found by tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);
            throw new TenantPropertiesServiceException("No one tenantProperties was found by tenantProperty guid: "
                    + tenantPropertyGuid + "and tenant guid: " + tenantGuid, ex);
        }
    }

    @Transactional
    @Override
    public ITenantProperties update(UUID tenantGuid, UUID tenantPropertyGuid, ITenantProperties iTenantProperty) throws TenantPropertiesServiceException {
        LOGGER.debug("Update Tenant property by tenant guid: {} and tenantProperty guid: {}; tenantProperty: {}", tenantGuid, tenantPropertyGuid, iTenantProperty);

        // find tenant property for updating
        final TenantProperties tenantProperty = (TenantProperties) get(tenantGuid, tenantPropertyGuid);

        // update tenant property
        if (iTenantProperty.getKey() != null) {
            tenantProperty.setKey(iTenantProperty.getKey());
        }
        if (iTenantProperty.getValue() != null) {
            tenantProperty.setValue(iTenantProperty.getValue());
        }

        final PropertyType propertyType = (PropertyType) propertyTypeService.createOrUpdate(iTenantProperty.getPropertyType());
        tenantProperty.setPropertyType(propertyType);

        // save updated tenant property
        try {
            return tenantPropertiesRepository.save(tenantProperty);
        } catch (ConstraintViolationException | DataAccessException ex) {
            LOGGER.error("Tenant property wasn't updated for tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);
            throw new TenantPropertiesServiceException("Tenant wasn't updated for tenant guid: "
                    + tenantGuid + "and tenantProperty guid: " + tenantPropertyGuid, ex);
        }
    }

    @Transactional
    @Override
    public void delete(UUID tenantGuid, UUID tenantPropertyGuid) throws TenantPropertiesServiceException {
        LOGGER.debug("Delete Tenant property by tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);

        final TenantProperties tenantProperties = (TenantProperties) get(tenantGuid, tenantPropertyGuid);
        try {
            tenantPropertiesRepository.delete(tenantProperties);
        } catch (DataAccessException ex) {
            LOGGER.error("Tenant Property was not deleted from DB: " + tenantProperties, ex);
            throw new UserServiceException("An error occurred while deleting the tenant property from the Database", ex);
        }

        LOGGER.debug("Tenant property was deleted for tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);
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

        final PropertyType propertyType = (PropertyType) propertyTypeService.createOrUpdate(iTenantProperties.getPropertyType());
        tenantProperties.setPropertyType(propertyType);
//        tenantProperties.setTenant(iTenantProperties.getTenant());

        return tenantProperties;
    }
}
