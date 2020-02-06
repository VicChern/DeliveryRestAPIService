package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.OrderEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEventRepository extends CrudRepository<OrderEvent, Long> {
}
