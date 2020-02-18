package com.softserve.itacademy.kek.models;

import java.util.UUID;

import com.softserve.itacademy.kek.models.impl.Tenant;

public interface IOrder {

    Tenant getTenant();

    UUID getGuid();

    String getSummary();

    IOrderDetails getOrderDetails();
}
