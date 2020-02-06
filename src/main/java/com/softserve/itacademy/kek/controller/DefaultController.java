package com.softserve.itacademy.kek.controller;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
    final Logger logger = Logger.getLogger(DefaultController.class);

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
        logger.info("Sending the error message to the client");
        return ResponseEntity.ok(response.toString());
    }
}
