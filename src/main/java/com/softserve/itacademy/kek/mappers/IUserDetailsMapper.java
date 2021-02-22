package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.DetailsDto;
import com.softserve.itacademy.kek.models.IUserDetails;
import com.softserve.itacademy.kek.models.impl.UserDetails;

/**
 * Interface for {@link UserDetails} mapping
 */
@Mapper
public interface IUserDetailsMapper {

    IUserDetailsMapper INSTANCE = Mappers.getMapper(IUserDetailsMapper.class);

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
    UserDetails toUserDetails(IUserDetails iUserDetails);
}
