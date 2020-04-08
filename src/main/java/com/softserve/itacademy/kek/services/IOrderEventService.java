package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import com.softserve.itacademy.kek.exception.OrderEventServiceException;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.Order;

/**
 * Service interface for {@link IOrderEvent}
 */
public interface IOrderEventService {
    /**
     * Inserts new {@link IOrderEvent} for orderGuid and user guid
     *
     * @param orderGuid   order guid
     * @param userGuid    user guid
     * @param iOrderEvent order event
     * @return saved order event
     * @throws OrderEventServiceException if an error occurred
     */
    IOrderEvent create(UUID orderGuid, UUID userGuid, IOrderEvent iOrderEvent) throws OrderEventServiceException;

    /**
     * Gets order event by {@link IOrderEvent} guid
     *
     * @param guid {@link IOrderEvent} guid
     * @return guid
     * @throws OrderEventServiceException if an error occurred
     */
    IOrderEvent getByGuid(UUID guid) throws OrderEventServiceException;

    /**
     * Gets last added order event by order guid
     *
     * @param orderGuid order guid
     * @return last added order event by order guid
     * @throws OrderEventServiceException if an error occurred
     */
    IOrderEvent getLastAddedEvent(UUID orderGuid) throws OrderEventServiceException;

    /**
     * Checks if {@link IOrderEvent} can be tracked
     *
     * @param orderGuid order guid
     * @return if {@link IOrderEvent} can be tracked
     * @throws OrderEventServiceException if an error occurred
     */
    Boolean ifOrderEventCanBeTracked(UUID orderGuid) throws OrderEventServiceException;

    /**
     * Gets all OrderEvents for current {@link Order} order
     *
     * @param orderGuid {@link Order} guid
     * @return all OrderEvents for order
     * @throws OrderEventServiceException if an error occurred
     */
    List<IOrderEvent> getAllEventsForOrder(UUID orderGuid) throws OrderEventServiceException;

    /**
     * Gets all {@link IOrderEvent} that is delivering now
     *
     * @return all {@link IOrderEvent} that is delivering now
     * @throws OrderEventServiceException if an error occurred
     */
    List<IOrderEvent> findAllThatDeliveringNow() throws OrderEventServiceException;
}
