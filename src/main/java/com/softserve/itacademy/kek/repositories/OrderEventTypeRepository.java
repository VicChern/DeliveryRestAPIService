package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.impl.OrderEventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEventTypeRepository extends JpaRepository<OrderEventType, Long> {

    OrderEventType findByName(String name);
}
