package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderEvent;

/**
 * Service interface for {@link IOrderEvent}
 */
public interface IOrderEventService {

//    /**
//     * Saves order event
//     *
//     * @param orderEvent {@link OrderEvent}
//     * @param orderGuid  {@link Order} guid
//     * @return saved {@link OrderEvent} orderEvent
//     */
//    IOrderEvent create(IOrderEvent orderEvent, UUID orderGuid);

    /**
     * Gets order event by {@link IOrderEvent} guid
     *
     * @param guid {@link IOrderEvent} guid
     * @return guid
     */
    IOrderEvent getByGuid(UUID guid);

    /**
     * Gets all OrderEvents for current {@link Order} order
     *
     * @param orderGuid {@link Order} guid
     * @return all OrderEvents for order
     */
    List<IOrderEvent> getAllEventsForOrder(UUID orderGuid);
}
