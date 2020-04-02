package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.OrderEventDto;
import com.softserve.itacademy.kek.models.IOrderEvent;


public interface IOrderEventMapper {

    OrderEventDto fromIOrderEvent(IOrderEvent orderEvent);

}
