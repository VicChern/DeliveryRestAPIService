package com.softserve.itacademy.kek.controller;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

// TODO: Add logger

@RestController
public class DefaultController {

    /**
     * Handles all the exception which were not handled locally
     *
     * @param e the exception which occurred anywhere in the code
     * @return the error message as a JSON
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> defaultExceptionHandler(Exception e) {
        JSONObject response = new JSONObject()
                .put("Error", "Something went wrong...");
        return ResponseEntity.ok(response.toString());
    }
}
