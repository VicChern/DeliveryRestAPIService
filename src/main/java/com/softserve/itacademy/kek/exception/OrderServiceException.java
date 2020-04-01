package com.softserve.itacademy.kek.exception;

/**
 * Exception for {@link com.softserve.itacademy.kek.services.IOrderService}
 */
public class OrderServiceException extends ServiceException {

    public OrderServiceException(String message) {
        super(message);
    }

    public OrderServiceException(String message, Exception ex) {
        super(message, ex);
    }
}
