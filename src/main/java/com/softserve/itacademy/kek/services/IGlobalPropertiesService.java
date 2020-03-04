package com.softserve.itacademy.kek.services;

import java.util.List;

import com.softserve.itacademy.kek.models.IGlobalProperties;
import com.softserve.itacademy.kek.models.impl.GlobalProperties;

/**
 * Service interface for {@link IGlobalProperties}
 */
public interface IGlobalPropertiesService {

    /**
     * Saved new {@link GlobalProperties} for customer with customerGuid to db
     *
     * @param globalProperties globalProperties
     * @return saved globalProperties
     */
    IGlobalProperties create(IGlobalProperties globalProperties);

    /**
     * Updates {@link GlobalProperties} by key
     *
     * @param globalProperties globalProperties
     * @param key              key
     * @return updated globalProperties
     */
    IGlobalProperties update(IGlobalProperties globalProperties, String key);

    /**
     * Gets globalProperties by {@link GlobalProperties} key
     *
     * @param key key
     * @return globalProperties by key
     */
    IGlobalProperties getByKey(String key);

    /**
     * Gets all globalProperties
     *
     * @return a list of all globalProperties
     */
    List<IGlobalProperties> getAll();

    /**
     * Deletes globalProperties by {@link GlobalProperties} key
     *
     * @param key key
     */
    void deleteByKey(String key);
}
