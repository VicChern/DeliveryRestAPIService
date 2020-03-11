package com.softserve.itacademy.kek.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.GlobalProperty;

public interface GlobalPropertiesRepository extends JpaRepository<GlobalProperty, Long> {

    GlobalProperty findByIdProperty(Long id);

    GlobalProperty findByKey(String key);

    void deleteByKey(String key);
}
