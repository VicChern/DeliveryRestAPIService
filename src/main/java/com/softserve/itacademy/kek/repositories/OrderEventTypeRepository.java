package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.OrderEventType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEventTypeRepository extends CrudRepository<OrderEventType, Long> {
}
