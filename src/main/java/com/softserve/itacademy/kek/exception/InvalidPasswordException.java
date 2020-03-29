package com.softserve.itacademy.kek.exception;

import javax.validation.constraints.NotNull;

public class InvalidPasswordException extends ServiceException {

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(@NotNull String message, @NotNull Exception ex) {
        super(message, ex);
    }
}
