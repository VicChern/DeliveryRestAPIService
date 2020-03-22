package com.softserve.itacademy.kek.exception;

import javax.validation.constraints.NotNull;

public class IdentityServiceException extends ServiceException {

    public IdentityServiceException(@NotNull String message, @NotNull Exception ex) {
        super(message, ex);
    }
}
