package com.softserve.itacademy.kek.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByGuid(UUID guid);

    void deleteByGuid(UUID guid);
}
