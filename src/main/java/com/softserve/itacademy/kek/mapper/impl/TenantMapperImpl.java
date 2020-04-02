package com.softserve.itacademy.kek.mapper.impl;

import com.softserve.itacademy.kek.dto.TenantDetailsDto;
import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.mapper.ITenantMapper;
import com.softserve.itacademy.kek.models.ITenant;

public class TenantMapperImpl implements ITenantMapper {
    @Override
    public TenantDto fromITenant(ITenant tenant) {
        TenantDetailsDto tenantDetailsDto = new TenantDetailsDto(tenant.getTenantDetails().getPayload(), tenant.getTenantDetails().getImageUrl());
        TenantDto tenantDto = new TenantDto(tenant.getGuid(), tenant.getOwner(), tenant.getName(), tenantDetailsDto);
        return tenantDto;
    }
}
