package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
