package com.softserve.itacademy.kek.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByGuid(UUID guid);
    List<Order> findAllByTenantGuid(UUID guid);
}
