package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.services.ITenantService;

import javax.validation.constraints.NotNull;

/**
 * Exception for {@link ITenantService}
 */
public class TenantServiceException extends ServiceException {

    public TenantServiceException(@NotNull String message, Exception ex) {
        super(message, ex);
    }
}
