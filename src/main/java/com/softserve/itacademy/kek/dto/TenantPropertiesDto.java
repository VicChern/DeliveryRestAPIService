package com.softserve.itacademy.kek.dto;

import java.util.Objects;
import java.util.UUID;

import com.softserve.itacademy.kek.models.IPropertyType;
import com.softserve.itacademy.kek.models.ITenantProperties;

public class TenantPropertiesDto implements ITenantProperties {
    private UUID guid;

    private PropertyTypeDto propertyType;

    private String key;

    private String value;

    public TenantPropertiesDto() {
    }

    public TenantPropertiesDto(UUID guid, PropertyTypeDto type, String key, String value) {
        this.guid = guid;
        this.propertyType = type;
        this.key = key;
        this.value = value;
    }

    @Override
    public UUID getGuid() {
        return guid;
    }

    @Override
    public IPropertyType getPropertyType() {
        return propertyType;
    }


    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public void setPropertyType(PropertyTypeDto propertyType) {
        this.propertyType = propertyType;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TenantPropertiesDTO{" +
                "guid='" + guid + '\'' +
                ", type='" + propertyType + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TenantPropertiesDto)) return false;
        TenantPropertiesDto that = (TenantPropertiesDto) o;
        return guid.equals(that.guid) &&
                propertyType.equals(that.propertyType) &&
                key.equals(that.key) &&
                value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, propertyType, key, value);
    }
}
