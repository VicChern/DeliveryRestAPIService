package com.softserve.itacademy.kek.services;

import java.util.List;

import com.softserve.itacademy.kek.models.IGlobalProperty;
import com.softserve.itacademy.kek.models.impl.GlobalProperty;

/**
 * Service interface for {@link IGlobalProperty}
 */
public interface IGlobalPropertiesService {

    /**
     * Saved new {@link GlobalProperty} for customer with customerGuid to db
     *
     * @param globalProperty globalProperties
     * @return saved globalProperties
     */
    IGlobalProperty create(IGlobalProperty globalProperty);

    /**
     * Updates {@link GlobalProperty} by key
     *
     * @param globalProperty globalProperties
     * @return updated globalProperties
     */
    IGlobalProperty update(IGlobalProperty globalProperty);

    /**
     * Gets globalProperties by {@link GlobalProperty} key
     *
     * @param key key
     * @return globalProperties by key
     */
    IGlobalProperty getByKey(String key);

    /**
     * Gets all globalProperties
     *
     * @return a list of all globalProperties
     */
    List<IGlobalProperty> getAll();

    /**
     * Deletes globalProperties by {@link GlobalProperty} key
     *
     * @param key key
     */
    void deleteByKey(String key);
}
