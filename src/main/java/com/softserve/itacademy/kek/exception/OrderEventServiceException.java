package com.softserve.itacademy.kek.exception;

public class OrderEventServiceException extends ServiceException {

    public OrderEventServiceException(String message) {
        super(message);
    }

    public OrderEventServiceException(String message, Exception ex) {
        super(message, ex);
    }
}
