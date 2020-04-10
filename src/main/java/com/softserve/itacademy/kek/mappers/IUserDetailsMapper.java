package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import com.softserve.itacademy.kek.dto.DetailsDto;
import com.softserve.itacademy.kek.models.IUserDetails;
import com.softserve.itacademy.kek.models.impl.UserDetails;

/**
 * Interface for {@link UserDetails} mapping
 */
@Mapper
public interface IUserDetailsMapper {

    /**
     * Transform {@link IUserDetails} to {@link DetailsDto}
     *
     * @param iUserDetails
     * @return detailsDto
     */
    @Named("toDto")
    DetailsDto toUserDetailsDto(IUserDetails iUserDetails);

    /**
     * Transform {@link IUserDetails} to {@link UserDetails}
     *
     * @param iUserDetails
     * @return detailsDto
     */
    @Named("toEntity")
    UserDetails toUserDetails(IUserDetails iUserDetails);
}
