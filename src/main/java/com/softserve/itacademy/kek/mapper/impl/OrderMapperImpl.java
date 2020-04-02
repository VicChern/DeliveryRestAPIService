package com.softserve.itacademy.kek.mapper.impl;

import com.softserve.itacademy.kek.dto.OrderDetailsDto;
import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.mapper.IOrderMapper;
import com.softserve.itacademy.kek.models.IOrder;

public class OrderMapperImpl implements IOrderMapper {
    @Override
    public OrderDto fromIOrder(IOrder order) {

        OrderDetailsDto orderDetailsDto = new OrderDetailsDto(order.getOrderDetails().getPayload(), order.getOrderDetails().getImageUrl());
        OrderDto orderDto = new OrderDto(order.getGuid(), order.getTenant().getGuid(), order.getSummary(), orderDetailsDto);

        return orderDto;
    }
}
