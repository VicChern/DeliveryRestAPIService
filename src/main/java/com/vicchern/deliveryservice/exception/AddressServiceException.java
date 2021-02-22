package com.vicchern.deliveryservice.exception;

import com.vicchern.deliveryservice.services.impl.AddressServiceImpl;

/**
 * Exception for {@link AddressServiceImpl}
 */
public class AddressServiceException extends ServiceException {

    public AddressServiceException(String message) {
        super(message);
    }

    public AddressServiceException(String message, Exception ex) {
        super(message, ex);
    }
}
