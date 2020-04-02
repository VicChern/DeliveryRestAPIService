package com.softserve.itacademy.kek.exception;

import javax.validation.constraints.NotNull;

public class InvalidCredentialsException extends ServiceException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(@NotNull String message, @NotNull Exception ex) {
        super(message, ex);
    }
}
