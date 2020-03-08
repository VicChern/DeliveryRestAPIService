package com.softserve.itacademy.kek.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.dto.RegistrationDto;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.services.IUserService;

@RestController
public class RegistrationController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IUserService userService;

    @Autowired
    public RegistrationController(IUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public PasswordEncoder encoder;

    @GetMapping("/registration")
    public void userRegistration(@RequestBody @Valid RegistrationDto userData) throws Exception {
        logger.info("Created request for user registration: {}", userData);

        User user = new User();
        String encodedPassword = encoder.encode(userData.getPassword());


        user.setName(userData.getName());
        user.setNickname(userData.getNickname());
        user.setEmail(userData.getEmail());
        user.setPhoneNumber(userData.getPhoneNumber());


        boolean isAlreadyRegistered = false;

        // here should be database check for existing user by email

        if ( isAlreadyRegistered ) {
            userService.create(user);

            // save password separately

            logger.info("User has been added to DB {}", user);
        } else {
            logger.info("User already exists in DB {}", user);
            /**
             * need to add custom exception type
             */
            throw new Exception();
        }
    }
}
