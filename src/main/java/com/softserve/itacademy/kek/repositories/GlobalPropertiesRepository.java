package com.softserve.itacademy.kek.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.GlobalProperty;

/**
 * Repository for work with GlobalProperties
 */
public interface GlobalPropertiesRepository extends JpaRepository<GlobalProperty, Long> {
//todo refactor all of this
    /**
     * Retrieves an Global Properties by its id.
     *
     * @param id
     * @return the Global Property with the given id
     */
    GlobalProperty findByIdProperty(Long id);

    /**
     * Retrieves an Global Properties by its id.
     *
     * @param key
     * @return the Global Property with the given key
     */
    GlobalProperty findByKey(String key);

    void deleteByKey(String key);
}
