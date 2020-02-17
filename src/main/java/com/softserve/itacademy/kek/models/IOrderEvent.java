package com.softserve.itacademy.kek.models;

import java.util.UUID;

public interface IOrderEvent {

    IOrder getIdOrder();

    IActor getIdActor();

    IOrderEventType getIdOrderEventType();

    UUID getGuid();

    String getPayload();
}
