package com.softserve.itacademy.kek.models.enums;

/**
 * Enum of event types for {@link com.softserve.itacademy.kek.models.impl.OrderEventType}
 */
public enum EventTypeEnum {

    /**
     * event type for order event that is created
     */
    CREATED,

    /**
     * event type for order event that is assigned by currier
     */
    ASSIGNED,

    /**
     * event type for order event that is delivering
     */
    STARTED,

    /**
     * event type for order event that is delivered already
     */
    DELIVERED
}
