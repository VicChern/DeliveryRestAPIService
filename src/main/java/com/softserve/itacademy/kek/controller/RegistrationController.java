package com.softserve.itacademy.kek.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.controller.utils.KekPaths;
import com.softserve.itacademy.kek.dto.RegistrationDto;
import com.softserve.itacademy.kek.dto.TokenDto;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.services.IAuthenticationService;
import com.softserve.itacademy.kek.services.ITokenService;
import com.softserve.itacademy.kek.services.IUserService;

@RestController
public class RegistrationController extends DefaultController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private final IAuthenticationService authenticationService;
    private final IUserService userService;
    private final ITokenService tokenService;

    @Autowired
    public RegistrationController(IAuthenticationService authenticationService,
                                  IUserService userService,
                                  ITokenService tokenService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping(path = KekPaths.REGISTRATION, consumes = KekMediaType.REGISTRATION_USER,
            produces = KekMediaType.TOKEN)
    public ResponseEntity<TokenDto> userRegistration(@RequestBody @Valid RegistrationDto userData) {
        logger.info("Creating request for user registration: {}", userData);

        IUser createdUser = userService.create(userData, userData.getPassword());
        TokenDto token = new TokenDto(tokenService.getToken(createdUser.getEmail()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(token);
    }
}
