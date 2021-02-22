package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.TenantDetailsDto;
import com.softserve.itacademy.kek.models.ITenantDetails;
import com.softserve.itacademy.kek.models.impl.TenantDetails;

/**
 * Interface for {@link TenantDetails} mapping
 */
@Mapper
public interface ITenantDetailsMapper {

    ITenantDetailsMapper INSTANCE = Mappers.getMapper(ITenantDetailsMapper.class);

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
    TenantDetails toTenantDetails(ITenantDetails iTenantDetails);
}
