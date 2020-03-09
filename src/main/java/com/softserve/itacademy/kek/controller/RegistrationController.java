package com.softserve.itacademy.kek.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.dto.RegistrationDto;
import com.softserve.itacademy.kek.services.ICreateUserService;

@RestController
public class RegistrationController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ICreateUserService createUser;

    @Autowired
    public RegistrationController(ICreateUserService createUser) {
        this.createUser = createUser;
    }

    @GetMapping("/registration")
    public void userRegistration(@RequestBody @Valid RegistrationDto userData) throws Exception {
        logger.info("Created request for user registration: {}", userData);

        createUser.createNewUser(userData);
    }
}
