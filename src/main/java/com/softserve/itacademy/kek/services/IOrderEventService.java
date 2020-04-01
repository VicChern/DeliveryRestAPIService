package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import com.softserve.itacademy.kek.exception.OrderEventServiceException;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderEvent;

/**
 * Service interface for {@link IOrderEvent}
 */
public interface IOrderEventService {

    /**
     * Saves order event
     *
     * @param orderEvent {@link OrderEvent}
     * @param orderGuid  {@link Order} guid
     * @return saved {@link OrderEvent} orderEvent
     */
    IOrderEvent create(IOrderEvent orderEvent, UUID orderGuid) throws OrderEventServiceException;

    /**
     * Saves new {@link IOrderEvent} for orderGuid and customer guid
     *
     * @param orderGuid    orderGuid
     * @param customerGuid customerGuid
     * @param iOrderEvent  order event
     * @return saved order event
     */
    IOrderEvent createOrderEvent(UUID orderGuid, UUID customerGuid, IOrderEvent iOrderEvent) throws OrderEventServiceException;

    /**
     * Gets order event by {@link IOrderEvent} guid
     *
     * @param guid {@link IOrderEvent} guid
     * @return guid
     */
    IOrderEvent getByGuid(UUID guid) throws OrderEventServiceException;

    /**
     * Gets last added order event by order guid
     *
     * @param orderGuid order guid
     * @return last added order event by order guid
     */
    IOrderEvent getLastAddedEvent(UUID orderGuid) throws OrderEventServiceException;

    /**
     * Checks if {@link IOrderEvent} can be tracked
     *
     * @param orderGuid order guid
     * @return if {@link IOrderEvent} can be tracked
     */
    Boolean ifOrderEventCanBeTracked(UUID orderGuid) throws OrderEventServiceException;

    /**
     * Gets all OrderEvents for current {@link Order} order
     *
     * @param orderGuid {@link Order} guid
     * @return all OrderEvents for order
     */
    List<IOrderEvent> getAllEventsForOrder(UUID orderGuid) throws OrderEventServiceException;

    /**
     * Gets all {@link IOrderEvent} that is delivering now
     *
     * @return all {@link IOrderEvent} that is delivering now
     */
    List<IOrderEvent> findAllThatDeliveringNow() throws OrderEventServiceException;
}
