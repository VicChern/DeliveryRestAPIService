package com.softserve.itacademy.kek.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.dto.ErrorListDto;
import com.softserve.itacademy.kek.exception.ServiceException;

@RestController
public class DefaultController {
    private final static Logger logger = LoggerFactory.getLogger(DefaultController.class);

    /**
     * ServiceException handler.
     *
     * @param ex      ServiceException for handling.
     * @param request HttpServletRequest which caused the ServiceException.
     * @return ResponseEntity with HttpStatus and exception message in header.
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorListDto> serviceExceptionHandler(ServiceException ex, HttpServletRequest request) {
        logger.trace("IP: {}:{}:{} : EXCEPTION: {}", request.getRemoteHost(), request.getRemotePort(), request.getRemoteUser(), ex);

        ErrorListDto errorListDto = new ErrorListDto();
        errorListDto.addError(ex.getMessage());

        logger.warn("Sending the error message to the client");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorListDto);
    }

    /**
     * Handles all the exception which were not handled locally
     *
     * @param ex the exception which occurred anywhere in the code
     * @return the error message as a JSON
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorListDto> defaultExceptionHandler(Exception ex) {
        logger.error("An error occurred:", ex);

        ErrorListDto errorListDto = new ErrorListDto();
        errorListDto.addError(ex.getMessage());

        logger.warn("Sending the error message to the client");
        return ResponseEntity
                .status(HttpStatus.I_AM_A_TEAPOT)
                .body(errorListDto);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorListDto> validationExceptionHandler(Exception ex) {
        logger.error("An error occurred:", ex);

        ErrorListDto errorListDto = new ErrorListDto();
        errorListDto.addError(ex.getMessage());

        logger.warn("Sending the error message to the client");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorListDto);
    }
}
