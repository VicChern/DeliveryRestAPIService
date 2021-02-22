package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.services.impl.UserServiceImpl;

/**
 * Exception for {@link UserServiceImpl}
 */
public class UserServiceException extends ServiceException {

    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException(String message, Exception ex) {
        super(message, ex);
    }

    public UserServiceException(String message, int errorCode) {
        super(message, errorCode);
    }
}
