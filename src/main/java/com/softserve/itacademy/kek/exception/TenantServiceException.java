package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.services.ITenantService;

/**
 * Exception for {@link ITenantService}
 */
public class TenantServiceException extends BadRequestException {

    public TenantServiceException(String message) {
        super(message);
    }
}
