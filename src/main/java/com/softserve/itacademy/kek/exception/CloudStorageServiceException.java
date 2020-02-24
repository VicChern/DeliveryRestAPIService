package com.softserve.itacademy.kek.exception;

public class CloudStorageServiceException extends KekException {

    public CloudStorageServiceException(String message) {
        super(message);
    }

    public CloudStorageServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudStorageServiceException(Throwable cause) {
        super(cause);
    }
}