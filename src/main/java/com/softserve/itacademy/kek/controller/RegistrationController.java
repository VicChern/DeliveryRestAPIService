package com.softserve.itacademy.kek.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.controller.utils.KekMappingValues;
import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.dto.RegistrationDto;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.services.IAuthenticationService;
import com.softserve.itacademy.kek.services.ICreateUserService;

@RestController
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private final IAuthenticationService authenticationService;
    private final ICreateUserService createUser;

    @Autowired
    public RegistrationController(IAuthenticationService authenticationService, ICreateUserService createUser) {
        this.authenticationService = authenticationService;
        this.createUser = createUser;
    }

    @PostMapping(path = KekMappingValues.REGISTRATION, consumes = KekMediaType.REGISTRATION_USER,
            produces = KekMediaType.REGISTRATION_USER)
    public ResponseEntity userRegistration(@RequestBody @Valid RegistrationDto userData, HttpServletResponse response)  {
        logger.info("Creating request for user registration: {}", userData);

        final IUser user = createUser.createNewUser(userData);

        return new ResponseEntity(HttpStatus.OK);
    }
}
