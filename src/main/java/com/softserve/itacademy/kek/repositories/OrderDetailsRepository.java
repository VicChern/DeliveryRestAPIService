package com.softserve.itacademy.kek.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softserve.itacademy.kek.models.OrderDetails;

public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long> {
}
