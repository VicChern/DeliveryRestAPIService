package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
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
     * @param userDetails
     * @return detailsDto
     */
    DetailsDto toUserDetailsDto(IUserDetails userDetails);
}
