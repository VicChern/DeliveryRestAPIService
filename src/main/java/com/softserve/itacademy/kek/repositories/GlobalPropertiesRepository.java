package com.softserve.itacademy.kek.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.GlobalProperties;

public interface GlobalPropertiesRepository extends JpaRepository<GlobalProperties, Long> {

    GlobalProperties findByKey(String key);
}
