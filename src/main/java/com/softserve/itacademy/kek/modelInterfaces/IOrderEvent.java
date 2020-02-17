package com.softserve.itacademy.kek.modelInterfaces;

import com.softserve.itacademy.kek.models.Actor;
import com.softserve.itacademy.kek.models.Order;
import com.softserve.itacademy.kek.models.OrderEventType;

import java.util.UUID;

public interface IOrderEvent {

    Long getIdOrderEvent();

    public Order getIdOrder();

    Actor getIdActor();

    OrderEventType getIdOrderEventType();

    UUID getGuid();

    String getPayload();
}
