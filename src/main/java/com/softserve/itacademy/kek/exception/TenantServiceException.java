package com.softserve.itacademy.kek.exception;

import javax.validation.constraints.NotNull;

import com.softserve.itacademy.kek.services.ITenantService;

/**
 * Exception for {@link ITenantService}
 */
public class TenantServiceException extends ServiceException {

    public TenantServiceException(Exception ex, @NotNull String message) {
        super(ex, message);
    }
}
