package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.models.ITenant;


public interface ITenantMapper {

    TenantDto fromITenant(ITenant tenant);
}
