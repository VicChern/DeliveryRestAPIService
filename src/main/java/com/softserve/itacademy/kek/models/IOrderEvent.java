package com.softserve.itacademy.kek.models;

import java.util.UUID;

/**
 * Interface for OrderEvent data exchange with service and public api layers
 */
public interface IOrderEvent {

    /**
     * Returns order
     *
     * @return order
     */
    IOrder getOrder();

    /**
     * Returns actor
     *
     * @return actor
     */
    IActor getActor();

    /**
     * Returns order event type
     *
     * @return order event type
     */
    IOrderEventType getOrderEventType();

    /**
     * Returns order event guid
     *
     * @return order event guid
     */
    UUID getGuid();

    /**
     * Returns order event payload
     *
     * @return order event payload
     */
    String getPayload();
}
