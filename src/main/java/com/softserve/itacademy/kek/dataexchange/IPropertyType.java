package com.softserve.itacademy.kek.dataexchange;

/**
 * Interface for PropertyType data exchange with service layer
 */
public interface IPropertyType {

    /**
     * Returns PropertyType name
     *
     * @return PropertyType name
     */
    String getName();

    /**
     * Returns PropertyType schema
     *
     * @return PropertyType schema
     */
    String getSchema();

}
