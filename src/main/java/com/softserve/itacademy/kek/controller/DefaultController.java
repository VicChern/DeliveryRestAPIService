package com.softserve.itacademy.kek.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    /**
     * Handles all the exception which were not handled locally
     *
     * @param e the exception which occurred anywhere in the code
     * @return the error message as a JSON
     */
    @ExceptionHandler(Exception.class)
    public String defaultExceptionHandler(Exception e) {
        return "Error: something went wrong...";
    }
}
