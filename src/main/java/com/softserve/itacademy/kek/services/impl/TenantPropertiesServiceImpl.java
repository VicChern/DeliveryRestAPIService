package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.modelInterfaces.ITenantProperties;
import com.softserve.itacademy.kek.models.TenantProperties;
import com.softserve.itacademy.kek.services.ITenantPropertiesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TenantPropertiesServiceImpl implements ITenantPropertiesService {

    @Override
    public List<ITenantProperties> getTenantPropertiesByTenantGuid(UUID tenantGuid) {
        return null;
    }

    @Override
    public List<ITenantProperties> createTenantPropertiesForTenant(List<ITenantProperties> tenantProperties, UUID tenantGuid) {
        return null;
    }

    @Override
    public ITenantProperties getTenantPropertyForTenantByGuid(UUID tenantGuid, UUID tenantPropertyGuid) {
        return null;
    }

    @Override
    public ITenantProperties updateTenantPropertyForTenantByGuid(UUID tenantGuid, UUID tenantPropertyGuid, ITenantProperties tenantProperty) {
        return null;
    }

    @Override
    public void deleteTenantPropertyForTenantByGuid(UUID tenantGuid, UUID tenantProperty) {

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
