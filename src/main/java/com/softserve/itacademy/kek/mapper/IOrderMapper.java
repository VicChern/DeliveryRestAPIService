package com.softserve.itacademy.kek.mapper;

import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.impl.Order;

/**
 * Interface for {@link Order} mapping
 */
public interface IOrderMapper {

    /**
     * Transform {@link IOrder} to {@link OrderDto}
     *
     * @param order
     * @return orderDto
     */
    OrderDto fromIOrder(IOrder order);

}
