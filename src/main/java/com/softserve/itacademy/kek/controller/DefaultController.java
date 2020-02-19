package com.softserve.itacademy.kek.controller;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.dto.ErrorListDto;

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
    public ResponseEntity<ErrorListDto> defaultExceptionHandler(Exception ex) {
        logger.error("An error occurred:", ex);
        ErrorListDto errors = new ErrorListDto(new LinkedList<>());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.softserve.errorList+json");

        errors.addError(ex.getMessage());

        logger.warn("Sending the error message to the client");
        return ResponseEntity
                .badRequest()
                .headers(headers)
                .body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorListDto> validationExceptionHandler(HttpMessageNotReadableException ex) {
        logger.error("An error occurred:", ex);

        ErrorListDto errors = new ErrorListDto(new LinkedList<>());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.softserve.error+json");

        errors.addError(ex.getMessage());

        logger.warn("Sending the error message to the client");
        return ResponseEntity
                .badRequest()
                .headers(headers)
                .body(errors);
    }

    /**
     * Handles the DTO validation exceptions
     *
     * @param ex caught exception
     * @return a ResponseEntity instance with the {@link ErrorListDto} object
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorListDto> dtoValidationHandler(MethodArgumentNotValidException ex) {
        logger.error("An error occurred:", ex);

        ErrorListDto errors = new ErrorListDto(new LinkedList<>());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.softserve.error+json");

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.addError(fieldError.getDefaultMessage(), fieldError.getField());
        }

        logger.warn("Sending the error message to the client");
        return ResponseEntity
                .badRequest()
                .headers(headers)
                .body(errors);
    }
}
