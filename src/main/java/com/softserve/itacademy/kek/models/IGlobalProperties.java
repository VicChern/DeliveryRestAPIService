package com.softserve.itacademy.kek.models;

/**
 * Interface for GlobalProperties data exchange with Service Layer
 */
public interface IGlobalProperties {

    /**
     * Returns globalProperties propertyType
     *
     * @return globalProperties propertyType
     */
    IPropertyType getPropertyType();

    /**
     * Returns globalProperties key
     *
     * @return globalProperties key
     */
    String getKey();

    /**
     * Returns globalProperties value
     *
     * @return globalProperties value
     */
    String getValue();
}
