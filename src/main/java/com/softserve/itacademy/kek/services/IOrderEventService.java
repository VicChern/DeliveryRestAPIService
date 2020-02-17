package com.softserve.itacademy.kek.services;

import java.util.UUID;

import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.Order;

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
