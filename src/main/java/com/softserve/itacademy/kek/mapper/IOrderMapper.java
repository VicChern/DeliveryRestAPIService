package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.models.IOrder;


public interface IOrderMapper {

    OrderDto fromIOrder(IOrder order);

}
