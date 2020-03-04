package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.Transient;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.ITenant;

public class OrderDto implements IOrder {

    @NotNull
    @JsonProperty("tenant")
    private UUID tenantGuid;

    private UUID guid;

    @NotNull
    @Size(max = 256)
    private String summary;

    @NotNull
    @JsonProperty("details")
    private OrderDetailsDto orderDetails;

    public OrderDto() {
    }

    public OrderDto(UUID guid, UUID tenant, String summary, OrderDetailsDto orderDetails) {
        this.tenantGuid = tenant;
        this.guid = guid;
        this.summary = summary;
        this.orderDetails = orderDetails;
    }

    public UUID getTenantGuid() {
        return tenantGuid;
    }

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public OrderDetailsDto getOrderDetails() {
        return orderDetails;
    }

    @Transient
    @Override
    public ITenant getTenant() {
        return new TenantDto(tenantGuid, null, null, null);
    }

    @Override
    public UUID getGuid() {
        return guid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(tenantGuid, orderDto.tenantGuid) &&
                Objects.equals(guid, orderDto.guid) &&
                Objects.equals(orderDetails, orderDto.orderDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantGuid, guid, orderDetails);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "tenant='" + tenantGuid + '\'' +
                ", guid='" + guid + '\'' +
                ", details=" + orderDetails +
                '}';
    }
}
