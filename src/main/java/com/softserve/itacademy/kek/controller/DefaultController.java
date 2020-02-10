package com.softserve.itacademy.kek.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
    final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    /**
     * Handles all the exception which were not handled locally
     *
     * @param ex the exception which occurred anywhere in the code
     * @return the error message as a JSON
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> defaultExceptionHandler(Exception ex) {
        logger.error("An error occurred:", ex);
        JSONObject response = new JSONObject()
                .put("Error", "Something went wrong...");
        logger.warn("Sending the error message to the client");
        return ResponseEntity.ok(response.toString());
    }
}
