package com.softserve.itacademy.kek.dto;

import java.util.Objects;

public class OrderEventDto {
    private String guid;
    private String orderId;
    private String payload;
    private OrderEventTypesDto type;

    public OrderEventDto(String guid, String orderId, String payload, OrderEventTypesDto type) {
        this.guid = guid;
        this.orderId = orderId;
        this.payload = payload;
        this.type = type;
    }

    public String getGuid() {
        return guid;
    }

    public String getOrderId() {
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
}
