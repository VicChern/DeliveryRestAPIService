package com.softserve.itacademy.kek.dto;

import java.util.Objects;

public class OrderDto {
    private String tenant;
    private String guid;
    private OrderDetailsDto details;

    public OrderDto(String tenant, String guid, OrderDetailsDto details) {
        this.tenant = tenant;
        this.guid = guid;
        this.details = details;
    }

    public String getTenant() {
        return tenant;
    }

    public String getGuid() {
        return guid;
    }

    public OrderDetailsDto getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(tenant, orderDto.tenant) &&
                Objects.equals(guid, orderDto.guid) &&
                Objects.equals(details, orderDto.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenant, guid, details);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "tenant='" + tenant + '\'' +
                ", guid='" + guid + '\'' +
                ", details=" + details +
                '}';
    }
}
