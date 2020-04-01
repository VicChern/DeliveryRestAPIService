package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.exception.PropertyTypeServiceException;
import com.softserve.itacademy.kek.models.IPropertyType;

public interface IPropertyTypeService {
    /**
     * Inserts property type into DB
     *
     * @param propertyType property type data
     * @return inserted property type
     * @throws PropertyTypeServiceException
     */
    IPropertyType create(IPropertyType propertyType) throws PropertyTypeServiceException;

    /**
     * Updates property type in DB
     *
     * @param propertyType property type data
     * @return updated property type
     * @throws PropertyTypeServiceException
     */
    IPropertyType update(IPropertyType propertyType) throws PropertyTypeServiceException;

    /**
     * Gets property type by name
     *
     * @param name name of property type
     * @return property type
     * @throws PropertyTypeServiceException
     */
    IPropertyType getByName(String name) throws PropertyTypeServiceException;

    /**
     * Produces property type.
     * If type doesn't exist in DB it will be created.
     * If type exists in DB it will be updated.
     *
     * @param propertyType property type data
     * @return property type from db
     */
    IPropertyType produce(IPropertyType propertyType) throws PropertyTypeServiceException;
}
