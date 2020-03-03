package com.softserve.itacademy.kek.dto;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderListDto {
    @Valid
    private List<OrderDto> orderList;

    public OrderListDto() {
        this(new LinkedList<>());
    }

    public OrderListDto(List<OrderDto> orderList) {
        this.orderList = orderList;
    }

    public List<OrderDto> getOrderList() {
        return orderList;
    }

    public OrderListDto addOrder(OrderDto order) {
        orderList.add(order);

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderListDto)) return false;
        OrderListDto that = (OrderListDto) o;
        return Objects.equals(orderList, that.orderList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderList);
    }

    @Override
    public String toString() {
        return "OrderListDto{"
                + "orderList="
                + orderList.stream().map(OrderDto::toString).collect(Collectors.joining(","))
                + '}';
    }
}
