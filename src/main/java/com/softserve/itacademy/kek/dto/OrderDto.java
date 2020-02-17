package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class OrderDto {

    @NotNull
    private String tenant;

    @NotNull
    private String user;

    @NotNull
    private String guid;

    @NotNull
    @Size(max = 256)
    private String summary;

    @NotNull
    private OrderDetailsDto details;

    public OrderDto() {
    }

    public OrderDto(String tenant, String user, String guid, String summary, OrderDetailsDto details) {
        this.tenant = tenant;
        this.user = user;
        this.guid = guid;
        this.summary = summary;
        this.details = details;
    }

    public String getSummary() {
        return summary;
    }

    public String getUser() {
        return user;
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
