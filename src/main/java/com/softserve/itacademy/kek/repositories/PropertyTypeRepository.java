package com.softserve.itacademy.kek.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.PropertyType;

public interface PropertyTypeRepository extends JpaRepository<PropertyType, Long> {
    /**
     * Gets property type from DB
     *
     * @param name name of property type
     * @return property type
     */
    Optional<PropertyType> getByName(String name);
}
