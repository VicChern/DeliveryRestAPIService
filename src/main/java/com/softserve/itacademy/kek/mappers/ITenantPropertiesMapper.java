package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.TenantPropertiesDto;
import com.softserve.itacademy.kek.models.ITenantProperties;
import com.softserve.itacademy.kek.models.impl.TenantProperties;

/**
 * Interface for {@link TenantProperties} mapping
 */
@Mapper(uses = IPropertyTypeMapper.class)
public interface ITenantPropertiesMapper {

    ITenantPropertiesMapper INSTANCE = Mappers.getMapper(ITenantPropertiesMapper.class);

    /**
     * Transform {@link ITenantProperties} to {@link TenantPropertiesDto}
     *
     * @param tenantProperties
     * @return tenantPropertiesDto
     */
    TenantPropertiesDto toTenantPropertiesDto(ITenantProperties tenantProperties);
}
