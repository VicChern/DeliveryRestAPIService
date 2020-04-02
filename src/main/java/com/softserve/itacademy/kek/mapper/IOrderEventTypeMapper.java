package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.OrderEventTypesDto;
import com.softserve.itacademy.kek.models.IOrderEventType;
import com.softserve.itacademy.kek.models.impl.OrderEventType;

/**
 * Interface for {@link OrderEventType} mapping
 */
public interface IOrderEventTypeMapper {

    /**
     * Transform {@link IOrderEventType} to {@link OrderEventTypesDto}
     *
     * @param orderEventType
     * @return orderEventTypesDto
     */
    OrderEventTypesDto fromIOrderEventType(IOrderEventType orderEventType);
}
