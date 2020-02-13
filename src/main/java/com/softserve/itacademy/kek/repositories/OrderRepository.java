package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, Long> {

    Order findByGuid(UUID guid);

    void deleteByGuid(UUID guid);
}
