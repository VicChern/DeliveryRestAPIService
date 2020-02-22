package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.Order;

import java.util.UUID;

/**
 * Service interface for {@link IOrderEvent}
 */
public interface IOrderEventService {

    /**
     * Saves order event
     *
     * @param orderEvent
     * @param orderGuid  {@link Order} guid
     * @return
     */
    IOrderEvent create(IOrderEvent orderEvent, UUID orderGuid);

    /**
     * Gets order event by {@link IOrderEvent} guid
     *
     * @param guid {@link IOrderEvent} guid
     * @return guid
     */
    IOrderEvent getByGuid(UUID guid);
}
