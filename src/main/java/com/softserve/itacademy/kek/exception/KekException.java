package com.softserve.itacademy.kek.exception;

public class KekException extends RuntimeException {
    public KekException() {
    }

    public KekException(String message) {
        super(message);
    }

    public KekException(String message, Throwable cause) {
        super(message, cause);
    }

    public KekException(Throwable cause) {
        super(cause);
    }
}
