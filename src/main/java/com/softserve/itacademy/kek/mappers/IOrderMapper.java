package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.impl.Order;

/**
 * Interface for {@link Order} mapping
 */
@Mapper
public interface IOrderMapper {

    IOrderMapper INSTANCE = Mappers.getMapper(IOrderMapper.class);

    /**
     * Transform {@link IOrder} to {@link OrderDto}
     *
     * @param order
     * @return orderDto
     */
    OrderDto toOrderDto(IOrder order);

}
