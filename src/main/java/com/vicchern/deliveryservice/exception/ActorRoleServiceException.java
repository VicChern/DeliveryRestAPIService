package com.vicchern.deliveryservice.exception;

import com.vicchern.deliveryservice.services.impl.ActorRoleServiceImpl;

/**
 * Exception for {@link ActorRoleServiceImpl}
 */
public class ActorRoleServiceException extends ServiceException {

    public ActorRoleServiceException(String message, Exception ex) {
        super(message, ex);
    }
}
