package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.exception.PropertyTypeServiceException;
import com.softserve.itacademy.kek.models.IPropertyType;

public interface IPropertyTypeService {
    /**
     * Creates or updates property type.
     * If type doesn't exist in DB it will be created.
     * If type exists in DB it will be updated.
     * @param typeData property type data
     * @return property type from db
     */
    IPropertyType createOrUpdate(IPropertyType typeData) throws PropertyTypeServiceException;
}
