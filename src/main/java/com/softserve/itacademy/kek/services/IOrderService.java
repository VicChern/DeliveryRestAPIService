package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import com.softserve.itacademy.kek.exception.OrderServiceException;
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
     * @throws OrderServiceException
     */
    IOrder create(IOrder order, UUID customerGuid) throws OrderServiceException;

    /**
     * Gets all orders
     *
     * @return a list of all orders
     * @throws OrderServiceException
     */
    List<IOrder> getAll() throws OrderServiceException;

    /**
     * Gets order by {@link Order} guid
     *
     * @param guid {@link Order} guid
     * @return guid
     * @throws OrderServiceException
     */
    IOrder getByGuid(UUID guid) throws OrderServiceException;

    /**
     * Gets list of orders by {@link Tenant} guid
     *
     * @param guid {@link Tenant} guid
     * @return list of Orders
     * @throws OrderServiceException
     */

    List<IOrder> getAllByTenantGuid(UUID guid) throws OrderServiceException;

    /**
     * Updates {@link Order}
     *
     * @param order
     * @param guid  {@link Order} guid
     * @return updated order
     * @throws OrderServiceException
     */
    IOrder update(IOrder order, UUID guid) throws OrderServiceException;

    /**
     * Deletes order by {@link Order} guid
     *
     * @param guid
     * @throws OrderServiceException
     */
    void deleteByGuid(UUID guid) throws OrderServiceException;
}
