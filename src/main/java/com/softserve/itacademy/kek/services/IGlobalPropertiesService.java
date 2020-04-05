package com.softserve.itacademy.kek.services;

import java.util.List;

import com.softserve.itacademy.kek.exception.GlobalPropertiesServiceException;
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
     * @throws GlobalPropertiesServiceException
     */
    IGlobalProperty create(IGlobalProperty globalProperty) throws GlobalPropertiesServiceException;

    /**
     * Updates {@link GlobalProperty} by key
     *
     * @param globalProperty globalProperties
     * @return updated globalProperties
     * @throws GlobalPropertiesServiceException
     */
    IGlobalProperty update(IGlobalProperty globalProperty) throws GlobalPropertiesServiceException;

    /**
     * Gets globalProperties by {@link GlobalProperty} key
     *
     * @param key key
     * @return globalProperties by key
     * @throws GlobalPropertiesServiceException
     */
    IGlobalProperty getByKey(String key) throws GlobalPropertiesServiceException;

    /**
     * Gets all globalProperties
     *
     * @return a list of all globalProperties
     * @throws GlobalPropertiesServiceException
     */
    List<IGlobalProperty> getAll() throws GlobalPropertiesServiceException;

    /**
     * Deletes globalProperties by {@link GlobalProperty} key
     *
     * @param key key
     * @throws GlobalPropertiesServiceException
     */
    void deleteByKey(String key) throws GlobalPropertiesServiceException;
}
