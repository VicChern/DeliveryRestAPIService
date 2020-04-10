package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import com.softserve.itacademy.kek.dto.TenantDetailsDto;
import com.softserve.itacademy.kek.models.ITenantDetails;
import com.softserve.itacademy.kek.models.impl.TenantDetails;

/**
 * Interface for {@link TenantDetails} mapping
 */
@Mapper
public interface ITenantDetailsMapper {

    /**
     * Transform {@link ITenantDetails} to {@link TenantDetailsDto}
     *
     * @param iTenantDetails
     * @return tenantDetailsDto
     */
    @Named("toDto")
    TenantDetailsDto toTenantDetailsDto(ITenantDetails iTenantDetails);

    /**
     * Transform {@link ITenantDetails} to {@link TenantDetails}
     *
     * @param iTenantDetails
     * @return tenantDetails
     */
    @Named("toEntity")
    TenantDetails toTenantDetails(ITenantDetails iTenantDetails);
}
