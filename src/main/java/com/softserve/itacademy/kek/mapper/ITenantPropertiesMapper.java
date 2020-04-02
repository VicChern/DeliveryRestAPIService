package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.TenantPropertiesDto;
import com.softserve.itacademy.kek.models.ITenantProperties;


public interface ITenantPropertiesMapper {

    TenantPropertiesDto fromITenantProperties(ITenantProperties tenantProperties);
}
