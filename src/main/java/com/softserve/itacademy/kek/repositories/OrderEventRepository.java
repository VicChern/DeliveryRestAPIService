package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.OrderEvent;
import org.springframework.data.repository.CrudRepository;

public interface OrderEventRepository extends CrudRepository<OrderEvent, Long> {
}
