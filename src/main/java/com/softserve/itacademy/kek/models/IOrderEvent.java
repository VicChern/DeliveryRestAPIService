package com.softserve.itacademy.kek.models;

import java.util.UUID;

public interface IOrderEvent {

    IOrder getOrder();

    IActor getActor();

    IOrderEventType getOrderEventType();

    UUID getGuid();

    String getPayload();
}
