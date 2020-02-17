package com.softserve.itacademy.kek.dataexchange;

import com.softserve.itacademy.kek.models.Tenant;

import java.util.UUID;

public interface IOrder {

    Long getIdOrder();

    Tenant getTenant();

    UUID getGuid();

    String getSummary();

    IOrderDetails getOrderDetails();
}
