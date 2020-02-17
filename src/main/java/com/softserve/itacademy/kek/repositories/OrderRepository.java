package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByGuid(UUID guid);
}
