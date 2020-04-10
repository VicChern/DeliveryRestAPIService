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
     * @param iTenant
     * @return tenantDto
     */
    @Mapping(target = "tenantDetails", qualifiedByName = "toDto")
    TenantDto toTenantDto(ITenant iTenant);

    /**
     * Transform {@link ITenant} to {@link Tenant}
     *
     * @param iTenant
     * @return tenant
     */
    @Mapping(target = "tenantDetails", ignore = true)
    @Mapping(target = "tenantOwner", ignore = true)
    Tenant toTenant(ITenant iTenant);

}
