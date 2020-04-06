package com.softserve.itacademy.kek.dto;

import java.util.Objects;

import com.softserve.itacademy.kek.models.IPropertyType;

public class PropertyTypeDto implements IPropertyType {

    private String name;

    private String schema;

    public PropertyTypeDto() {
    }

    public PropertyTypeDto(String name, String schema) {
        this.name = name;
        this.schema = schema;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PropertyTypeDto)) return false;
        PropertyTypeDto that = (PropertyTypeDto) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(schema, that.schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, schema);
    }

    @Override
    public String toString() {
        return "PropertyTypeDto{" +
                "name='" + name + '\'' +
                ", schema='" + schema + '\'' +
                '}';
    }
}
