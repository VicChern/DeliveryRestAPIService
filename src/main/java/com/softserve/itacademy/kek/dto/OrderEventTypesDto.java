package com.softserve.itacademy.kek.dto;

import com.softserve.itacademy.kek.models.IOrderEventType;

public class OrderEventTypesDto implements IOrderEventType {

    public static final String CREATED = "CREATED";
    public static final String ASSIGNED = "ASSIGNED";
    public static final String STARTED = "STARTED";
    public static final String DELIVERED = "DELIVERED";

    //TODO: fix it
    @Override
    public String getName() {
        return null;
    }
}
