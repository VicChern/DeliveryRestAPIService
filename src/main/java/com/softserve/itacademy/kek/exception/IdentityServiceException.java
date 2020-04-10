package com.softserve.itacademy.kek.exception;

import javax.validation.constraints.NotNull;

import com.softserve.itacademy.kek.services.impl.IdentityServiceImpl;

/**
 * Exception for {@link IdentityServiceImpl}
 */
public class IdentityServiceException extends ServiceException {

    public IdentityServiceException(@NotNull String message, @NotNull Exception ex) {
        super(message, ex);
    }
}
