package com.softserve.itacademy.kek.mapper.impl;

import com.softserve.itacademy.kek.dto.OrderEventTypesDto;
import com.softserve.itacademy.kek.mapper.IOrderEventTypeMapper;
import com.softserve.itacademy.kek.models.IOrderEventType;

public class OrderEventTypeMapperImpl implements IOrderEventTypeMapper {

    @Override
    public OrderEventTypesDto fromIOrderEventType(IOrderEventType orderEventType) {
        return OrderEventTypesDto.valueOf(orderEventType.getName());
    }
}
