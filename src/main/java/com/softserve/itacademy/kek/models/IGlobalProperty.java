package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.models.impl.GlobalProperty;

/**
 * Interface for GlobalProperties data exchange with service and public api layers
 */
public interface IGlobalProperty {

    /**
     * Returns {@link GlobalProperty} id
     *
     * @return globalProperties id
     */
    Long getIdProperty();

    /**
     * Returns {@link GlobalProperty} propertyType
     *
     * @return globalProperties propertyType
     */
    IPropertyType getPropertyType();

    /**
     * Returns {@link GlobalProperty} key
     *
     * @return globalProperties key
     */
    String getKey();

    /**
     * Returns {@link GlobalProperty} value
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
