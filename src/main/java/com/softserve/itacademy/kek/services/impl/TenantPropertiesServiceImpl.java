package com.softserve.itacademy.kek.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.TenantPropertiesServiceException;
import com.softserve.itacademy.kek.mappers.ITenantPropertiesMapperImpl;
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

    private final static Logger logger = LoggerFactory.getLogger(TenantPropertiesServiceImpl.class);

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
    public List<ITenantProperties> getAllForTenant(UUID tenantGuid) throws TenantPropertiesServiceException {
        logger.info("Get all properties for tenant from DB: tenantGuid = {}", tenantGuid);

        try {
            final List<? extends ITenantProperties> tenantProperties = tenantPropertiesRepository.findAll();

            logger.debug("All properties for tenant was read from DB: {}", tenantProperties);

            return (List<ITenantProperties>) tenantProperties;
        } catch (Exception ex) {
            logger.error("Error while getting all properties for tenant from DB: tenantGuid = " + tenantGuid, ex);
            throw new TenantPropertiesServiceException("An error occurs while getting tenant properties", ex);
        }
    }

    @Transactional
    @Override
    public List<ITenantProperties> create(List<ITenantProperties> tenantProperties, UUID tenantGuid)
            throws TenantPropertiesServiceException {
        logger.info("Insert tenant properties into DB: tenantGuid = {}, iTenantProperties = {}",
                tenantGuid, tenantProperties);

        final List<TenantProperties> actualTenantProperties = new ArrayList<>();

        final Tenant updatedTenant;

        try {
            for (ITenantProperties p : tenantProperties) {
                TenantProperties property = ITenantPropertiesMapperImpl.INSTANCE.toTenantProperties(p);

                String typeName = p.getPropertyType().getName();
                PropertyType propertyType = (PropertyType) propertyTypeService.getByName(typeName);
                property.setPropertyType(propertyType);


                property.setGuid(UUID.randomUUID());
                actualTenantProperties.add(property);
            }

            final Tenant tenant = (Tenant) tenantService.getByGuid(tenantGuid);

            actualTenantProperties.forEach(tenant::addTenantProperty);

            updatedTenant = tenantRepository.saveAndFlush(tenant);

            logger.debug("Tenant properties were inserted into DB: {}", updatedTenant);
        } catch (Exception ex) {
            logger.error("Error while inserting tenant properties into DB: " + tenantProperties, ex);
            throw new TenantPropertiesServiceException("An error occurs while inserting tenant properties", ex);
        }

        // filter out only those tenant properties, that were saved
        Set<String> keys = tenantProperties
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
    public ITenantProperties getPropertyByTenantGuid(UUID tenantGuid, UUID tenantPropertyGuid)
            throws TenantPropertiesServiceException {
        logger.info("Get tenant property from DB: tenantGuid = {}, tenantPropertyGuid = {}",
                tenantGuid, tenantPropertyGuid);

        try {
            final TenantProperties tenantProperties =
                    tenantPropertiesRepository.findByGuidAndTenantGuid(tenantPropertyGuid, tenantGuid)
                            .orElseThrow(() -> new NoSuchElementException("Tenant property was not found"));

            logger.debug("Tenant property was read from DB: {}", tenantProperties);

            return tenantProperties;
        } catch (Exception ex) {
            logger.error("Error while getting tenant property from DB", ex);
            throw new TenantPropertiesServiceException("An error occurs while getting tenant property", ex);
        }
    }

    @Transactional
    @Override
    public ITenantProperties update(UUID tenantGuid, UUID tenantPropertyGuid, ITenantProperties iTenantProperty)
            throws TenantPropertiesServiceException {
        logger.info("Update tenant property in DB: tenantGuid = {}, tenantPropertyGuid = {}",
                tenantGuid, tenantPropertyGuid);

        final TenantProperties tenantProperty = (TenantProperties) getPropertyByTenantGuid(tenantGuid, tenantPropertyGuid);

        try {
            final String typeName = iTenantProperty.getPropertyType().getName();
            final PropertyType type = (PropertyType) propertyTypeService.getByName(typeName);

            tenantProperty.setPropertyType(type);

            if (iTenantProperty.getKey() != null) {
                tenantProperty.setKey(iTenantProperty.getKey());
            }
            if (iTenantProperty.getValue() != null) {
                tenantProperty.setValue(iTenantProperty.getValue());
            }

            final TenantProperties updatedProperties = tenantPropertiesRepository.saveAndFlush(tenantProperty);

            logger.debug("Tenant property was updated in DB: updatedProperties = {}", updatedProperties);

            return updatedProperties;
        } catch (Exception ex) {
            logger.error("Error while updating tenant property in DB: " + tenantProperty, ex);
            throw new TenantPropertiesServiceException("An error occurs while updating tenant properties", ex);
        }
    }

    @Transactional
    @Override
    public void delete(UUID tenantGuid, UUID tenantPropertyGuid) throws TenantPropertiesServiceException {
        logger.info("Delete tenant property from DB: tenantGuid = {}, tenantPropertyGuid = {}",
                tenantGuid, tenantPropertyGuid);

        final TenantProperties tenantProperty = (TenantProperties) getPropertyByTenantGuid(tenantGuid, tenantPropertyGuid);
        try {
            tenantPropertiesRepository.delete(tenantProperty);
            tenantPropertiesRepository.flush();

            logger.debug("Tenant property was deleted from DB: tenantProperty = {}", tenantProperty);
        } catch (Exception ex) {
            logger.error("Error while deleting tenant property from DB: " + tenantProperty, ex);
            throw new TenantPropertiesServiceException("An error occurred while deleting tenant property", ex);
        }
    }
}
