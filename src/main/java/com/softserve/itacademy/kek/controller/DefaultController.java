package com.softserve.itacademy.kek.controller;

import com.softserve.itacademy.kek.exception.ServiceException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DefaultController {
    final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    /**
     * ServiceException handler.
     * @param ex ServiceException for handling.
     * @param request HttpServletRequest which caused the ServiceException.
     * @return ResponseEntity with HttpStatus and exception message in header.
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<HttpHeaders> serviceExceptionHandler(ServiceException ex, HttpServletRequest request) {
        logger.trace("IP: {}:{}:{} : EXCEPTION: {}", request.getRemoteHost(), request.getRemotePort(), request.getRemoteUser(), ex);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Error", "Something went wrong: " + ex.getError()
                + "; path: " + request.getServletPath());

        logger.warn("Sending the error message to the client");
        return ResponseEntity.status(ex.getErrorCode()).headers(httpHeaders).build();
    }

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

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<String> validationExceptionHandler(Exception ex) {
        logger.error("An error occurred:", ex);
        logger.warn("Sending the error message to the client");
        return ResponseEntity.badRequest().body("Not valid request");
    }

}
