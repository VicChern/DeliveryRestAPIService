package com.softserve.itacademy.kek.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.OrderEventType;

/**
 * Repository for work with Order Event Types
 */
public interface OrderEventTypeRepository extends JpaRepository<OrderEventType, Long> {

    /**
     * Retrieves a order event type by its guid.
     *
     * @param name
     * @return the order event with the given name
     */
    OrderEventType findByName(String name);
}
