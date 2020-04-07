package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.OrderDetailsDto;
import com.softserve.itacademy.kek.models.IOrderDetails;
import com.softserve.itacademy.kek.models.impl.UserDetails;

/**
 * Interface for {@link UserDetails} mapping
 */
@Mapper
public interface IOrderDetailsMapper {

    /**
     * Transform {@link IOrderDetails} to {@link OrderDetailsDto}
     *
     * @param orderDetails
     * @return orderDetailsDto
     */
    OrderDetailsDto toOrderDetailsDto(IOrderDetails orderDetails);
}
