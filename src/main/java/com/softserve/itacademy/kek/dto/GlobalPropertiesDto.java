package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class GlobalPropertiesDto {

    @NotNull
    private PropertyTypeDto propertyType;

    @NotNull
    @Size(min = 1, max = 256)
    private String key;

    @NotNull
    @Size(min = 1, max = 4096)
    private String value;

    public GlobalPropertiesDto() {

    }

    public GlobalPropertiesDto(PropertyTypeDto propertyType, String key, String value) {
        this.propertyType = propertyType;
        this.key = key;
        this.value = value;
    }

    public PropertyTypeDto getPropertyType() {
        return propertyType;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlobalPropertiesDto that = (GlobalPropertiesDto) o;
        return Objects.equals(propertyType, that.propertyType) &&
                Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyType, key, value);
    }

    @Override
    public String toString() {
        return "GlobalProperties{" +
                "propertyTypeDto= " + propertyType +
                ", key= '" + key + '\'' +
                ", value= '" + value + '\'' +
                '}';
    }
}
