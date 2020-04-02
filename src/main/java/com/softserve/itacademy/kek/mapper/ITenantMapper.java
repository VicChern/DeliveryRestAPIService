package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.impl.Tenant;

/**
 * Interface for {@link Tenant} mapping
 */
public interface ITenantMapper {

    /**
     * Transform {@link ITenant} to {@link TenantDto}
     *
     * @param tenant
     * @return tenantDto
     */
    TenantDto fromITenant(ITenant tenant);
}
