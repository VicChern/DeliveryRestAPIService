package com.softserve.itacademy.kek.services;

import java.util.UUID;

import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.Order;

/**
 * Service interface for {@link IOrder}
 */
public interface IOrderService {

    /**
     * Saved new {@link Order} for customer with customerGuid to db
     *
     * @param order order
     * @param customerGuid customerGuid
     * @return saved order
     */
    IOrder create(IOrder order, UUID customerGuid);

    /**
     * Saved new {@link Order} for customer with customerGuid to db
     *
     * @param orderGuid orderGuid
     * @param actorGuid actorGuid
     * @param iOrderEvent order event
     * @return saved order
     */
    IOrderEvent createOrderEvent(UUID orderGuid, UUID actorGuid, IOrderEvent iOrderEvent);

    /**
     * Gets all orders
     *
     * @return a list of all orders
     */
    Iterable<IOrder> getAll();

    /**
     * Gets order by {@link Order} guid
     *
     * @param guid {@link Order} guid
     * @return guid
     */
    IOrder getByGuid(UUID guid);

    /**
     * Updates {@link Order}
     *
     * @param order
     * @param guid  {@link Order} guid
     * @return updated order
     */
    IOrder update(IOrder order, UUID guid);

    /**
     * Deletes order by {@link Order} guid
     *
     * @param guid
     */
    void deleteByGuid(UUID guid);
}
