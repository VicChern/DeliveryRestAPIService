package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.OrderDetailsDto;
import com.softserve.itacademy.kek.models.IOrderDetails;
import com.softserve.itacademy.kek.models.impl.OrderDetails;
import com.softserve.itacademy.kek.models.impl.UserDetails;

/**
 * Interface for {@link UserDetails} mapping
 */
@Mapper
public interface IOrderDetailsMapper {

    IOrderDetailsMapper INSTANCE = Mappers.getMapper(IOrderDetailsMapper.class);

    /**
     * Transform {@link IOrderDetails} to {@link OrderDetailsDto}
     *
     * @param orderDetails
     * @return orderDetailsDto
     */
    @Named("toDto")
    OrderDetailsDto toOrderDetailsDto(IOrderDetails orderDetails);

    /**
     * Transform {@link IOrderDetails} to {@link OrderDetails}
     *
     * @param orderDetails
     * @return orderDetails
     */
    @Named("toEntity")
    OrderDetails toOrderDetails(IOrderDetails orderDetails);
}
