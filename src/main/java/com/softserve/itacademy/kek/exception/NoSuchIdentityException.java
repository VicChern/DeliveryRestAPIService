package com.softserve.itacademy.kek.exception;

import javax.validation.constraints.NotNull;

public class NoSuchIdentityException extends ServiceException {

    public NoSuchIdentityException(@NotNull String message, @NotNull Exception ex) {
        super(message, ex);
    }
}
