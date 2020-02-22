package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.impl.GlobalProperties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalPropertiesRepository extends JpaRepository<GlobalProperties, Long> {
}
