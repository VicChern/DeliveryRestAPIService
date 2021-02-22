package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.services.impl.PropertyTypeServiceImpl;

/**
 * Exception for {@link PropertyTypeServiceImpl}
 */
public class PropertyTypeServiceException extends ServiceException {
    public PropertyTypeServiceException(String message, Exception ex) {
        super(message, ex);
    }
}
