package com.softserve.itacademy.kek.exception;

import javax.validation.constraints.NotNull;

import com.softserve.itacademy.kek.services.impl.GlobalPropertiesServiceImpl;

/**
 * Exception for {@link GlobalPropertiesServiceImpl}
 */
public class GlobalPropertiesServiceException extends ServiceException {

    public GlobalPropertiesServiceException(@NotNull String message, Exception ex) {
        super(message, ex);
    }
}
