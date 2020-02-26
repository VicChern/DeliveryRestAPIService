package com.softserve.itacademy.kek.exception;

import javax.validation.constraints.NotNull;

import com.softserve.itacademy.kek.services.ITenantPropertiesService;

/**
 * Exception for {@link ITenantPropertiesService}
 */
public class TenantPropertiesServiceException extends ServiceException {

    public TenantPropertiesServiceException(Exception ex, @NotNull String message) {
        super(ex, message);
    }
    public TenantPropertiesServiceException(String message) {
        super(null, message);
    }
}
