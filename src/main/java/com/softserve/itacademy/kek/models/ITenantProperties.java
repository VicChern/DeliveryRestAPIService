package com.softserve.itacademy.kek.models;

import java.util.UUID;

import com.softserve.itacademy.kek.dto.PropertyTypeDto;
import com.softserve.itacademy.kek.dto.TenantPropertiesDto;

/**
 * Interface for TenantProperties data exchange with service and public api layers
 */
public interface ITenantProperties {

    /**
     * Returns TenantProperty guid
     *
     * @return TenantProperty guid
     */
    UUID getGuid();

    /**
     * Returns IEventType for TenantProperty
     *
     * @return IEventType for TenantProperty
     */
    IPropertyType getPropertyType();

    /**
     * Returns TenantProperty key
     *
     * @return TenantProperty key
     */
    String getKey();

    /**
     * Returns TenantProperty value
     *
     * @return TenantProperty value
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

    /**
     * Transform {@link ITenantProperties} to {@link TenantPropertiesDto}
     *
     * @return tenantPropertiesDto
     */
    default TenantPropertiesDto transformProperty() {
        PropertyTypeDto propertyType = new PropertyTypeDto(
                getPropertyType().getName(),
                getPropertyType().getSchema());

        TenantPropertiesDto tenantPropertiesDto = new TenantPropertiesDto(
                getGuid(),
                propertyType,
                getKey(),
                getValue());
        return tenantPropertiesDto;
    }
}
