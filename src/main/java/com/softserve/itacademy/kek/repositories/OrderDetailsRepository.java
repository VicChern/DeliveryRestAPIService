package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.impl.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
}
