package com.softserve.itacademy.kek.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class PropertyTypeDto {

    @NotNull
    @Size(min = 1, max = 256)
    private String name;

    @NotNull
    @Size(min = 1)
    private String schema;

    public PropertyTypeDto() {
    }

    public String getName() {
        return name;
    }

    public String getSchema() {
        return schema;
    }

    public PropertyTypeDto(String name, String schema) {
        this.name = name;
        this.schema = schema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
        return "PropertyType{" +
                "name= '" + name + '\'' +
                ", schema= '" + schema + '\'' +
                '}';
    }
}
