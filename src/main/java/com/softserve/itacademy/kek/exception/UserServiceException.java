package com.softserve.itacademy.kek.exception;

/**
 * Exception for {@UserServiceImpl}
 */
public class UserServiceException extends BadRequestException {
    public UserServiceException(String message) {
        super(message);
    }
}
