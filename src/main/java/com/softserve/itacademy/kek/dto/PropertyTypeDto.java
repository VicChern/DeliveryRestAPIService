package com.softserve.itacademy.kek.dto;

import com.softserve.itacademy.kek.models.IPropertyType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PropertyTypeDto implements IPropertyType {

//    @NotNull
//    @Size(min = 1, max = 256)
    private String name;

//    @NotNull
//    @Size(min = 1)
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
