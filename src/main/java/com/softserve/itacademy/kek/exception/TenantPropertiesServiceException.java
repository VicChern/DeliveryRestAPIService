package com.softserve.itacademy.kek.exception;

import javax.validation.constraints.NotNull;

import com.softserve.itacademy.kek.services.ITenantPropertiesService;

/**
 * Exception for {@link ITenantPropertiesService}
 */
public class TenantPropertiesServiceException extends ServiceException {

    public TenantPropertiesServiceException(@NotNull String message, Exception ex) {
        super(message, ex);
    }

}
