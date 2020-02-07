package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.OrderDetails;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long> {
}
