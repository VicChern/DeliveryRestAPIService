package com.softserve.itacademy.kek.models;

import java.util.UUID;

public interface IOrder {

    ITenant getTenant();

    UUID getGuid();

    String getSummary();

    IOrderDetails getOrderDetails();
}
