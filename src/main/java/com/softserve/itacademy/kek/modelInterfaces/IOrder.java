package com.softserve.itacademy.kek.modelInterfaces;

import com.softserve.itacademy.kek.models.Tenant;

import java.util.UUID;

public interface IOrder {

    Long getIdOrder();

    Tenant getIdTenant();

    UUID getGuid();

    String getSummary();

    IOrderDetails getOrderDetails();
}
