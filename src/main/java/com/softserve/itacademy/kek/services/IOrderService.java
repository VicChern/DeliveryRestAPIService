package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.Tenant;

/**
 * Service interface for {@link IOrder}
 */
public interface IOrderService {

    /**
     * Saved new {@link Order} for customer with customerGuid to db
     *
     * @param order        order
     * @param customerGuid customerGuid
     * @return saved order
     */
    IOrder create(IOrder order, UUID customerGuid);

    /**
     * Gets all orders
     *
     * @return a list of all orders
     */
    List<IOrder> getAll();

    /**
     * Gets order by {@link Order} guid
     *
     * @param guid {@link Order} guid
     * @return guid
     */
    IOrder getByGuid(UUID guid);

    /**
     * Gets list of orders by {@link Tenant} guid
     *
     * @param guid {@link Tenant} guid
     * @return list of Orders
     */

    List<IOrder> getAllByTenantGuid(UUID guid);

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
