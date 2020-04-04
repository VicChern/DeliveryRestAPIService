package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.services.impl.ActorRoleServiceImpl;

/**
 * Exception for {@link ActorRoleServiceImpl}
 */
public class ActorRoleServiceException extends ServiceException {

    public ActorRoleServiceException(String message, Exception ex) {
        super(message, ex);
    }
}
