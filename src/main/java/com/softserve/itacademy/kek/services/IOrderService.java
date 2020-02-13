package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.modelInterfaces.IOrder;
import com.softserve.itacademy.kek.models.Order;

import java.util.UUID;

/**
 * Service interface for {@link IOrder}
 */

public interface IOrderService {

    /**
     * Saved new {@link Order} to db
     *
     * @param order order
     * @return saved order
     */
    IOrder save(IOrder order);

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
