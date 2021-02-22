package com.vicchern.deliveryservice.exception;

import com.vicchern.deliveryservice.services.impl.ActorServiceImpl;

/**
 * Exception for {@link ActorServiceImpl}
 */
public class ActorServiceException extends ServiceException {

    public ActorServiceException(String message, Exception ex) {
        super(message, ex);
    }
}