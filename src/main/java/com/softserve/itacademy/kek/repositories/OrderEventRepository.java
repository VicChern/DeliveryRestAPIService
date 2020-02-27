package com.softserve.itacademy.kek.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.impl.OrderEvent;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {

    OrderEvent findByGuid(UUID guid);

    List<OrderEvent> getOrderEventsByOrder(IOrder order);
}
