package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.dataexchange.IOrderEvent;

import java.util.UUID;

/**
 * Service interface for {@link IOrderEvent}
 */
public interface IOrderEventService {

    /**
     * Saved new {@link IOrderEvent} to db
     *
     * @param orderEvent order event
     * @return saved order event
     */
    IOrderEvent create(IOrderEvent orderEvent);

    /**
     * Gets order event by {@link IOrderEvent} guid
     *
     * @param guid {@link IOrderEvent} guid
     * @return guid
     */
    IOrderEvent getByGuid(UUID guid);
}
