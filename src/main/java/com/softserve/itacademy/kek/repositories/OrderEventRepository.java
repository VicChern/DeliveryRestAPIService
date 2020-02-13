package com.softserve.itacademy.kek.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softserve.itacademy.kek.models.OrderEvent;

public interface OrderEventRepository extends CrudRepository<OrderEvent, Long> {
}
