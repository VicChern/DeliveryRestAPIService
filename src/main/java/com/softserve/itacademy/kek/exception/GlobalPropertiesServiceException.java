package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.services.IGlobalPropertiesService;

import javax.validation.constraints.NotNull;

/**
 * Exception for {@link IGlobalPropertiesService}
 */
public class GlobalPropertiesServiceException extends ServiceException {

    public GlobalPropertiesServiceException(@NotNull String message, Exception ex) {
        super(message, ex);
    }
}
