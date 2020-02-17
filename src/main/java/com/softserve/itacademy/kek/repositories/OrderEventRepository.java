package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.OrderEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderEventRepository extends CrudRepository<OrderEvent, Long> {

    OrderEvent findByGuid(UUID guid);
}
