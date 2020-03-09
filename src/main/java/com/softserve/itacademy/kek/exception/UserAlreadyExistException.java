package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.services.impl.CreateUserServiceImpl;

/**
 * Exception for {@link CreateUserServiceImpl}
 */
public class UserAlreadyExistException extends BadRequestException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
