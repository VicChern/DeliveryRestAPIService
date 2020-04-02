package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.OrderEventDto;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEvent;

/**
 * Interface for {@link OrderEvent} mapping
 */
public interface IOrderEventMapper {

    /**
     * Transform {@link IOrderEvent} to {@link OrderEventDto}
     *
     * @param orderEvent
     * @return orderEventDto
     */
    OrderEventDto fromIOrderEvent(IOrderEvent orderEvent);

}
