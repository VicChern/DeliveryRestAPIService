package com.softserve.itacademy.kek.dto;

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
}
