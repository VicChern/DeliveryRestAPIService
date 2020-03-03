package com.softserve.itacademy.kek.dto;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderEventListDto {
    private UUID orderGuid;
    @Valid
    private List<OrderEventDto> orderEventList;

    public OrderEventListDto() {
    }

    public OrderEventListDto(UUID orderGuid) {
        this(orderGuid, new LinkedList<>());
    }

    public OrderEventListDto(UUID orderGuid, List<OrderEventDto> orderEventList) {
        this.orderGuid = orderGuid;
        this.orderEventList = orderEventList;
    }

    public UUID getOrderId() {
        return orderGuid;
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
        return Objects.equals(orderGuid, that.orderGuid) &&
                Objects.equals(orderEventList, that.orderEventList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderGuid, orderEventList);
    }

    @Override
    public String toString() {
        return "OrderEventListDto{"
                + "orderId='"
                + orderGuid + '\''
                + ", orderEventList="
                + orderEventList.stream().map(OrderEventDto::toString).collect(Collectors.joining(","))
                + '}';
    }
}
