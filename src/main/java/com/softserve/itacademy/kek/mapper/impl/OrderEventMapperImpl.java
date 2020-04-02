package com.softserve.itacademy.kek.mapper.impl;

import com.softserve.itacademy.kek.dto.OrderEventDto;
import com.softserve.itacademy.kek.mapper.IOrderEventMapper;
import com.softserve.itacademy.kek.mapper.IOrderEventTypeMapper;
import com.softserve.itacademy.kek.models.IOrderEvent;

public class OrderEventMapperImpl implements IOrderEventMapper {

    private final IOrderEventTypeMapper orderEventTypeMapper;

    public OrderEventMapperImpl(IOrderEventTypeMapper orderEventTypeMapper) {
        this.orderEventTypeMapper = orderEventTypeMapper;
    }

    @Override
    public OrderEventDto fromIOrderEvent(IOrderEvent orderEvent) {
        return new OrderEventDto(orderEvent.getGuid(),
                orderEvent.getOrder().getGuid(),
                orderEvent.getPayload(),
                orderEventTypeMapper.fromIOrderEventType(orderEvent.getOrderEventType()));
    }
}
