package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.impl.Tenant;

/**
 * Interface for {@link Tenant} mapping
 */
@Mapper(uses = ITenantDetailsMapper.class)
public interface ITenantMapper {

    ITenantMapper INSTANCE = Mappers.getMapper(ITenantMapper.class);

    /**
     * Transform {@link ITenant} to {@link TenantDto}
     *
     * @param tenant
     * @return tenantDto
     */
    @Mapping(target = "tenantDetails", qualifiedByName = "toDto")
    TenantDto toTenantDto(ITenant tenant);

    /**
     * Transform {@link ITenant} to {@link Tenant}
     *
     * @param tenant
     * @return tenant
     */
    @Mapping(target = "tenantDetails", qualifiedByName = "toEntity")
    Tenant toTenant(ITenant tenant);

}
