package com.softserve.itacademy.kek.exception;

/**
 * Exception for {@link com.softserve.itacademy.kek.services.IOrderService}
 */
public class OrderServiceException extends BadRequestException {

    public OrderServiceException(String message) {
        super(message);
    }
}
