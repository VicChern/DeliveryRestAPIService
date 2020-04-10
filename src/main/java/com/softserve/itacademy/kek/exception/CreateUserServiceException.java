package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.services.impl.CreateUserServiceImpl;
/**
 * Exception for {@link CreateUserServiceImpl}
 */
public class CreateUserServiceException extends ServiceException {

    public CreateUserServiceException(String message, Exception ex) {
        super(message, ex);
    }

    public CreateUserServiceException(String message, int errorCode) {
        super(message, errorCode);
    }
}
