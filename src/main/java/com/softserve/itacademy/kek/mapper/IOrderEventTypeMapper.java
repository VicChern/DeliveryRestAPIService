package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.OrderEventTypesDto;
import com.softserve.itacademy.kek.models.IOrderEventType;


public interface IOrderEventTypeMapper {

    OrderEventTypesDto fromIOrderEventType(IOrderEventType orderEventType);
}
