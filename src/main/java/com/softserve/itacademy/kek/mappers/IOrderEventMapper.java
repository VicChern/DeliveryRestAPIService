package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.OrderEventDto;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEvent;

/**
 * Interface for {@link OrderEvent} mapping
 */
@Mapper
public interface IOrderEventMapper {

    IOrderEventMapper INSTANCE = Mappers.getMapper(IOrderEventMapper.class);

    /**
     * Transform {@link IOrderEvent} to {@link OrderEventDto}
     *
     * @param orderEvent
     * @return orderEventDto
     */
    OrderEventDto toOrderEventDto(IOrderEvent orderEvent);

}
