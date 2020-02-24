package com.softserve.itacademy.kek.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.OrderEventType;

public interface OrderEventTypeRepository extends JpaRepository<OrderEventType, Long> {

    OrderEventType findByName(String name);
}
