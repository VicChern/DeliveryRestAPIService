package com.softserve.itacademy.kek.exception;

import javax.validation.constraints.NotNull;

public class NoSuchUserException extends ServiceException {
    public NoSuchUserException(@NotNull String message, @NotNull Exception ex) {
        super(message, ex);
    }
}
