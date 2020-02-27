package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.UUID;

import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.IOrderEventType;

public class OrderEventDto implements IOrderEvent {
    @NotNull
    private UUID guid;
    private OrderDto orderDto;


    @NotNull
    @Size(max = 1024)
    private String payload;
    private OrderEventTypesDto type;

    public OrderEventDto() {
    }

    public OrderEventDto(UUID guid, OrderDto orderDto, String payload, OrderEventTypesDto type) {
        this.guid = guid;
        this.orderDto = orderDto;
        this.payload = payload;
        this.type = type;
    }

    public UUID getGuid() {
        return guid;
    }

    public OrderDto getOrderId() {
        return orderDto;
    }

    public String getPayload() {
        return payload;
    }

    public OrderEventTypesDto getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEventDto that = (OrderEventDto) o;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(orderDto, that.orderDto) &&
                Objects.equals(payload, that.payload) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, orderDto, payload, type);
    }

    @Override
    public String toString() {
        return "OrderEventDto{" +
                "guid='" + guid + '\'' +
                ", orderId='" + orderDto + '\'' +
                ", payload='" + payload + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public IOrder getOrder() {
        return orderDto;
    }

//    @Override
//    public IActor getActor() {
//        return null;
//    }

    @Override
    public IOrderEventType getOrderEventType() {
        return type;
    }
}
