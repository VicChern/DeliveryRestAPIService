package com.softserve.itacademy.kek.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.IdentityType;
import com.softserve.itacademy.kek.models.impl.OrderEventType;

public interface IdentityTypeRepository extends JpaRepository<IdentityType, Long> {

    IdentityType findByName(String name);
}
