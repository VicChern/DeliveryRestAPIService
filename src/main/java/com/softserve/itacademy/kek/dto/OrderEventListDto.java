package com.softserve.itacademy.kek.dto;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderEventListDto {
    private String orderId;
    @Valid
    private List<OrderEventDto> orderEventList;

    public OrderEventListDto() {
    }

    public OrderEventListDto(String orderId) {
        this(orderId, new LinkedList<>());
    }

    public OrderEventListDto(String orderId, List<OrderEventDto> orderEventList) {
        this.orderId = orderId;
        this.orderEventList = orderEventList;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<OrderEventDto> getOrderEventList() {
        return orderEventList;
    }

    public OrderEventListDto addOrderEvent(OrderEventDto orderEvent) {
        orderEventList.add(orderEvent);

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderEventListDto)) return false;
        OrderEventListDto that = (OrderEventListDto) o;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(orderEventList, that.orderEventList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderEventList);
    }

    @Override
    public String toString() {
        return "OrderEventListDto{"
                + "orderId='"
                + orderId + '\''
                + ", orderEventList="
                + orderEventList.stream().map(OrderEventDto::toString).collect(Collectors.joining(","))
                + '}';
    }
}
