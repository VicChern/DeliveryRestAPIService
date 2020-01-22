package com.softserve.itacademy.kek.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DefaultController {

    /**
     * Handles all the exception which were not handled locally
     * @param e the exception which occurred anywhere in the code
     * @return the error message as a JSON
     */
    @ExceptionHandler(Exception.class)
    public Map<String, String> defaultExceptionHandler (Exception e) {
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("Error", "Oooops. Something went terribly wrong... =(");
        response.put("Error details", e.getMessage());

        return response;
    }
}
