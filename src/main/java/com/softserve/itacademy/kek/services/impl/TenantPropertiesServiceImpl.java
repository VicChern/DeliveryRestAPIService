package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.exception.TenantPropertiesServiceException;
import com.softserve.itacademy.kek.modelInterfaces.ITenantProperties;
import com.softserve.itacademy.kek.models.PropertyType;
import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.models.TenantProperties;
import com.softserve.itacademy.kek.repositories.TenantPropertiesRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.services.ITenantPropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TenantPropertiesServiceImpl implements ITenantPropertiesService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ITenantPropertiesService.class);

    private final TenantPropertiesRepository tenantPropertiesRepository;
    private final TenantRepository tenantRepository;

    @Autowired
    public TenantPropertiesServiceImpl(TenantPropertiesRepository tenantPropertiesRepository, TenantRepository tenantRepository) {
        this.tenantPropertiesRepository = tenantPropertiesRepository;
        this.tenantRepository = tenantRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ITenantProperties> getAllForTenant(UUID tenantGuid) {
        LOGGER.info("Getting all TenantProperties for tenant guid: {}", tenantGuid);

        List<ITenantProperties> tenantProperties = new ArrayList<>(tenantPropertiesRepository.findAll());

        if (tenantProperties.isEmpty()) {
            LOGGER.error("No one tenantProperties was found for tenant guid: {}", tenantGuid);
            throw new TenantPropertiesServiceException("No one tenantProperties was found for tenant guid: " + tenantGuid);
        }

        LOGGER.info("TenantProperties was found: {}", tenantProperties);

        return tenantProperties;
    }

    @Transactional
    @Override
    public List<ITenantProperties> create(List<ITenantProperties> iTenantProperties, UUID tenantGuid) {
        LOGGER.info("Save tenant properties for tenant guid {} to db: {}", tenantGuid, iTenantProperties);

        Tenant tenant = tenantRepository.findByGuid(tenantGuid);

        if(tenant == null) {
            LOGGER.error("There is no Tenant in db for tenant guid: {}", tenantGuid);
            throw new TenantPropertiesServiceException("Tenant wasn't found for tenant guid: " + tenantGuid);
        }

        List<TenantProperties> tenantProperties = new ArrayList<>();
        iTenantProperties.forEach(iTenantProperty -> tenantProperties.add(transform(iTenantProperty)));

        tenantProperties.forEach(tenant::addTenantProperty);

        try {
            tenant = tenantRepository.save(tenant);
        } catch (PersistenceException ex) {
            LOGGER.error("Tenant properties wasn't saved for tenant guid: {}, properties: {}", tenantGuid, iTenantProperties);
            throw new TenantPropertiesServiceException("Tenant properties wasn't saved for tenant guid: " + tenantGuid);
        }

        LOGGER.info("Tenant properties was saved for tenant guid: {}, properties: {}", tenantGuid, iTenantProperties);

        Set<String> keys = iTenantProperties
                .stream()
                .map(ITenantProperties::getKey)
                .collect(Collectors.toSet());

        return tenant.getTenantPropertiesList()
                .stream()
                .filter(tenantProperty -> keys.contains(tenantProperty.getKey()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ITenantProperties get(UUID tenantGuid, UUID tenantPropertyGuid) {
        LOGGER.info("Getting TenantProperties by tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);

        ITenantProperties tenantProperty = tenantPropertiesRepository.findByGuidAndTenantGuid(tenantPropertyGuid, tenantGuid);

        if (tenantProperty == null) {
            LOGGER.error("No one tenantProperties was found by tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);
            throw new TenantPropertiesServiceException("No one tenantProperties was found by tenantProperty guid: "
                    + tenantPropertyGuid + "and tenant guid: " + tenantGuid);
        }

        LOGGER.info("TenantProperties was found: {}", tenantProperty);

        return tenantProperty;
    }

    @Transactional
    @Override
    public ITenantProperties update(UUID tenantGuid, UUID tenantPropertyGuid, ITenantProperties iTenantProperty) {
        LOGGER.info("Update Tenant property by tenant guid: {} and tenantProperty guid: {}; tenantProperty: {}", tenantGuid, tenantPropertyGuid, iTenantProperty);

        // find tenant property for updating
        TenantProperties tenantProperty = tenantPropertiesRepository.findByGuidAndTenantGuid(tenantPropertyGuid, tenantGuid);
        if (tenantProperty == null) {
            LOGGER.error("No one tenantProperties for updating was found by tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);
            throw new TenantPropertiesServiceException("No one tenantProperties for updating was found by tenantProperty guid: "
                    + tenantPropertyGuid + "and tenant guid: " + tenantGuid);
        }

        // update tenant property
        PropertyType propertyType = (PropertyType) tenantProperty.getPropertyType();
        if (iTenantProperty.getPropertyType().getName() != null ) { propertyType.setName(iTenantProperty.getPropertyType().getName()); }
        if (iTenantProperty.getPropertyType().getSchema() != null ) { propertyType.setSchema(iTenantProperty.getPropertyType().getSchema()); }

        if (iTenantProperty.getKey() != null ) { tenantProperty.setKey(iTenantProperty.getKey()); }
        if (iTenantProperty.getValue() != null ) { tenantProperty.setValue(iTenantProperty.getValue()); }

        tenantProperty.setPropertyType(propertyType);

        // save updated tenant property
        TenantProperties updatedTenantProperty;
        try {
            updatedTenantProperty = tenantPropertiesRepository.save(tenantProperty);
        } catch (PersistenceException ex) {
            LOGGER.error("Tenant property wasn't updated for tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);
            throw new TenantPropertiesServiceException("Tenant wasn't updated for tenant guid: "
                    + tenantGuid + "and tenantProperty guid: " + tenantPropertyGuid);
        }

        LOGGER.info("Tenant property was updated: {}", updatedTenantProperty);
        return updatedTenantProperty;
    }

    @Transactional
    @Override
    public void delete(UUID tenantGuid, UUID tenantPropertyGuid) {
        LOGGER.info("Delete Tenant property by tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);

        if (!tenantPropertiesRepository.existsTenantPropertiesByGuidAndTenantGuid(tenantPropertyGuid, tenantGuid)) {
            LOGGER.error("No one tenantProperties was found for deleting by tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);
            throw new TenantPropertiesServiceException("No one tenantProperties was found for deleting by tenantProperty guid: "
                    + tenantPropertyGuid + "and tenant guid: " + tenantGuid);
        }

        tenantPropertiesRepository.removeByGuidAndTenantGuid(tenantPropertyGuid, tenantGuid);

        LOGGER.info("Tenant property was deleted for tenant guid: {} and tenantProperty guid: {}", tenantGuid, tenantPropertyGuid);
    }

    private TenantProperties transform(ITenantProperties iTenantProperties) {

        TenantProperties tenantProperties = new TenantProperties();

        tenantProperties.setGuid(iTenantProperties.getGuid());
        tenantProperties.setKey(iTenantProperties.getKey());
        tenantProperties.setValue(iTenantProperties.getValue());
        tenantProperties.setPropertyType(iTenantProperties.getPropertyType());
        tenantProperties.setTenant(iTenantProperties.getTenant());

        return tenantProperties;
    }
}
