package com.softserve.itacademy.kek.mapper.impl;

import com.softserve.itacademy.kek.dto.PropertyTypeDto;
import com.softserve.itacademy.kek.dto.TenantPropertiesDto;
import com.softserve.itacademy.kek.mapper.ITenantPropertiesMapper;
import com.softserve.itacademy.kek.models.ITenantProperties;

public class TenantPropertiesMapperImpl implements ITenantPropertiesMapper {
    @Override
    public TenantPropertiesDto fromITenantProperties(ITenantProperties tenantProperties) {
        PropertyTypeDto propertyType = new PropertyTypeDto(
                tenantProperties.getPropertyType().getName(),
                tenantProperties.getPropertyType().getSchema());

        TenantPropertiesDto tenantPropertiesDto = new TenantPropertiesDto(
                tenantProperties.getGuid(),
                propertyType,
                tenantProperties.getKey(),
                tenantProperties.getValue());
        return tenantPropertiesDto;
    }
}
