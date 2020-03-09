package com.softserve.itacademy.kek.exception;

import javax.validation.constraints.NotNull;

import com.softserve.itacademy.kek.services.impl.CreateUserServiceImpl;

/**
 * Exception for {@link CreateUserServiceImpl}
 */
public class UserAlreadyExistException extends ServiceException {
    public UserAlreadyExistException(String message) {
        super(message);
    }

    public UserAlreadyExistException(@NotNull String message, @NotNull Exception ex) {
        super(message, ex);
    }

    public UserAlreadyExistException(Throwable cause) {
        super(cause);
    }

    public UserAlreadyExistException(@NotNull Exception ex, @NotNull String error, @NotNull int errorCode, @NotNull String message) {
        super(ex, error, errorCode, message);
    }
}
