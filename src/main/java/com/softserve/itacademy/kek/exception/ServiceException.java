package com.softserve.itacademy.kek.exception;

import javax.validation.constraints.NotNull;

/**
 * Common exception for all services.
 */
public abstract class ServiceException extends RuntimeException {

    @NotNull
    private String error;

    @NotNull
    private int errorCode;

    public ServiceException(@NotNull String error, @NotNull int errorCode, @NotNull String message) {
        super(message);
        this.error = error;
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
