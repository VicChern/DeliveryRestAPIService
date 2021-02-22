package com.softserve.itacademy.kek.exception;

import  com.softserve.itacademy.kek.services.impl.OrderEventServiceImpl;

/**
 * Exception for {@link OrderEventServiceImpl}
 */
public class OrderEventServiceException extends ServiceException {

    public OrderEventServiceException(String message) {
        super(message);
    }

    public OrderEventServiceException(String message, Exception ex) {
        super(message, ex);
    }
}
