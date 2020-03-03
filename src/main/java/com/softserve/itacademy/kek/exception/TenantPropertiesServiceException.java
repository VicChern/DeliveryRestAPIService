package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.services.ITenantPropertiesService;

import javax.validation.constraints.NotNull;

/**
 * Exception for {@link ITenantPropertiesService}
 */
public class TenantPropertiesServiceException extends ServiceException {

    public TenantPropertiesServiceException(@NotNull String message, Exception ex) {
        super(message, ex);
    }

}
