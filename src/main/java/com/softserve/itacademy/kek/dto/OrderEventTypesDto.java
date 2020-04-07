package com.softserve.itacademy.kek.dto;

import com.softserve.itacademy.kek.models.IOrderEventType;
import com.softserve.itacademy.kek.models.enums.EventTypeEnum;

public enum OrderEventTypesDto implements IOrderEventType {
    CREATED(EventTypeEnum.CREATED.toString()),
    ASSIGNED(EventTypeEnum.ASSIGNED.toString()),
    STARTED(EventTypeEnum.STARTED.toString()),
    DELIVERED(EventTypeEnum.DELIVERED.toString());

    private String type;

    OrderEventTypesDto(String type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return type;
    }
}

