package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.controller.SseController;

/**
 * Exception for {@link SseController}
 */
public class TrackingException extends KekException {

    public TrackingException(String message) {
        super(message);
    }

}
