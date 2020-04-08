package com.softserve.itacademy.kek.exception;

public class CreateUserServiceException extends ServiceException {

    public CreateUserServiceException(String message, Exception ex) {
        super(message, ex);
    }

    public CreateUserServiceException(String message, int errorCode) {
        super(message, errorCode);
    }
}
