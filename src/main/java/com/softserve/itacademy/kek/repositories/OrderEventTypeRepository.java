package com.softserve.itacademy.kek.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softserve.itacademy.kek.models.OrderEventType;

public interface OrderEventTypeRepository extends CrudRepository<OrderEventType, Long> {
}
