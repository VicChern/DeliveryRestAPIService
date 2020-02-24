package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.services.ITenantPropertiesService;

/**
 * Exception for {@link ITenantPropertiesService}
 */
public class TenantPropertiesServiceException extends BadRequestException {

    public TenantPropertiesServiceException(String message) {
        super(message);
    }
}
