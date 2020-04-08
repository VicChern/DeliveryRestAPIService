package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.exception.PropertyTypeServiceException;
import com.softserve.itacademy.kek.models.IPropertyType;

/**
 * Service interface for types of properties
 */
public interface IPropertyTypeService {
    /**
     * Gets property type by name
     *
     * @param name name of property type
     * @return property type
     * @throws PropertyTypeServiceException
     */
    IPropertyType getByName(String name) throws PropertyTypeServiceException;
}
