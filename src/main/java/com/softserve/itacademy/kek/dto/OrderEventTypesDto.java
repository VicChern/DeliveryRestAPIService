package com.softserve.itacademy.kek.dto;

import com.softserve.itacademy.kek.models.IOrderEventType;

public enum OrderEventTypesDto implements IOrderEventType {
    CREATED("CREATED"),
    ASSIGNED("ASSIGNED"),
    STARTED("STARTED"),
    DELIVERED("DELIVERED");

    private String type;

    OrderEventTypesDto(String type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

