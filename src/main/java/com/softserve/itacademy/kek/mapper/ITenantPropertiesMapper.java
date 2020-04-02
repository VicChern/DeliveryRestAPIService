package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.TenantPropertiesDto;
import com.softserve.itacademy.kek.models.ITenantProperties;
import com.softserve.itacademy.kek.models.impl.TenantProperties;

/**
 * Interface for {@link TenantProperties} mapping
 */
public interface ITenantPropertiesMapper {

    /**
     * Transform {@link ITenantProperties} to {@link TenantPropertiesDto}
     *
     * @param tenantProperties
     * @return tenantPropertiesDto
     */
    TenantPropertiesDto fromITenantProperties(ITenantProperties tenantProperties);
}
