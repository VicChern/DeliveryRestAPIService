package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.Transient;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.softserve.itacademy.kek.models.IActor;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.IOrderEventType;

public class OrderEventDto implements IOrderEvent {

    private UUID guid;

    private UUID orderId;

    @NotNull
    @Size(max = 1024)
    private String payload;

    private OrderEventTypesDto type;

    public OrderEventDto() {
    }

    public OrderEventDto(UUID guid, UUID orderId, String payload, OrderEventTypesDto type) {
        this.guid = guid;
        this.orderId = orderId;
        this.payload = payload;
        this.type = type;
    }

    public UUID getGuid() {
        return guid;
    }

    public UUID getOrderId() {
        return orderId;
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
                Objects.equals(orderId, that.orderId) &&
                Objects.equals(payload, that.payload) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, orderId, payload, type);
    }

    @Override
    public String toString() {
        return "OrderEventDto{" +
                "guid='" + guid + '\'' +
                ", orderId='" + orderId + '\'' +
                ", payload='" + payload + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    @Transient
    public IOrder getOrder() {
        return new OrderDto(guid, orderId, null, null);
    }

    @Override
    @Transient
    public IActor getActor() {
        return null;
    }

    @Override
    @Transient
    public IOrderEventType getOrderEventType() {
        return type;
    }
}
