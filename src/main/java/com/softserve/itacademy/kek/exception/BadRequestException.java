package com.softserve.itacademy.kek.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Service exception with status 400.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends ServiceException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value(), message);
    }
}
