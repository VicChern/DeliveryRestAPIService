package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.services.impl.ActorServiceImpl;

/**
 * Exception for {@link ActorServiceImpl}
 */
public class ActorServiceException extends ServiceException {

    public ActorServiceException(String message, Exception ex) {
        super(message, ex);
    }
}