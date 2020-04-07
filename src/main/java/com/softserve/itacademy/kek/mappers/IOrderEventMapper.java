package com.softserve.itacademy.kek.mappers;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.OrderEventDto;
import com.softserve.itacademy.kek.dto.OrderEventTypesDto;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.IOrderEventType;
import com.softserve.itacademy.kek.models.impl.OrderEvent;

/**
 * Interface for {@link OrderEvent} mapping
 */
@Mapper
public interface IOrderEventMapper {

    IOrderEventMapper INSTANCE = Mappers.getMapper(IOrderEventMapper.class);

    /**
     * Returns order GUID
     *
     * @param order
     * @return UUID
     */
    @Named("getOrderGuid")
    default UUID getOrderGuid(IOrder order) {
        return order.getGuid();
    }

    /**
     * Transform {@link IOrderEventType} to {@link OrderEventTypesDto}
     *
     * @param orderEventType
     * @return OrderEventTypesDto
     */

    @Named("getOrderEventType")
    default OrderEventTypesDto getOrderEventType(IOrderEventType orderEventType){
        return OrderEventTypesDto.valueOf(orderEventType.getName());
    }

    @Mapping(source = "order", target = "orderId", qualifiedByName = "getOrderGuid")
    @Mapping(source = "orderEventType", target = "type", qualifiedByName = "getOrderEventType")
    OrderEventDto toOrderEventDto(IOrderEvent orderEvent);

}
