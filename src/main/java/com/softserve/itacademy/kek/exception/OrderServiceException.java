package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.services.IOrderService;

/**
 * Exception for {@link IOrderService}
 */
public class OrderServiceException extends ServiceException {

    public OrderServiceException(String message) {
        super(message);
    }

    public OrderServiceException(String message, Exception ex) {
        super(message, ex);
    }
}
