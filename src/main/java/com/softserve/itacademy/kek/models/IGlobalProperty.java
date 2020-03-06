package com.softserve.itacademy.kek.models;

/**
 * Interface for GlobalProperties data exchange with Service Layer
 */
public interface IGlobalProperty {

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

    /**
     * Returns Integer or something else
     * according to current property
     *
     * @param clazz
     * @param <T>
     * @return value
     */
    default <T> T getValue(Class<T> clazz) {
        if (clazz == Integer.class) {
            return (T) Integer.valueOf(getValue());
        }
        return (T) getValue();
    }
}
