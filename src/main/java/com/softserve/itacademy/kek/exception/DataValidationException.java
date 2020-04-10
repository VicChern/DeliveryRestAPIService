package com.softserve.itacademy.kek.exception;

import com.softserve.itacademy.kek.controller.utils.Validation.ContactNumberValidator;

/**
 * Exception for {@link ContactNumberValidator}
 */
public class DataValidationException extends ServiceException {

    public DataValidationException(String message) {
        super(message);
    }
}
