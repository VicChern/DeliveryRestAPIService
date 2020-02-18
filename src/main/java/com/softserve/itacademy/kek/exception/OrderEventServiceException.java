package com.softserve.itacademy.kek.exception;

public class OrderEventServiceException extends BadRequestException {

    public OrderEventServiceException(String message) {
        super(message);
    }
}
