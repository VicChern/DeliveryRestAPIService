package com.softserve.itacademy.kek.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.Order;

/**
 * Repository for work with Tenant property types
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves an order by its guid.
     *
     * @param guid
     * @return the order with the given guid
     */
    Order findByGuid(UUID guid);

    /**
     * Retrieves a list of orders by its guid.
     *
     * @param guid
     * @return the list of orders
     */
    List<Order> findAllByTenantGuid(UUID guid);
}
