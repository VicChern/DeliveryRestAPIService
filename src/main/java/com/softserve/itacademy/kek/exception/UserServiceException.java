package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.services.impl.UserServiceImpl;

/**
 * Exception for {@link UserServiceImpl}
 */
public class UserServiceException extends BadRequestException {
    public UserServiceException(String message) {
        super(message);
    }
}
