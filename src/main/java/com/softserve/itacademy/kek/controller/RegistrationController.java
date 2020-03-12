package com.softserve.itacademy.kek.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.dto.RegistrationDto;
import com.softserve.itacademy.kek.dto.SessionDto;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.services.IAuthenticationService;
import com.softserve.itacademy.kek.services.ICreateUserService;

@RestController
public class RegistrationController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IAuthenticationService authenticationService;
    private final ICreateUserService createUser;

    @Autowired
    public RegistrationController(IAuthenticationService authenticationService, ICreateUserService createUser) {
        this.authenticationService = authenticationService;
        this.createUser = createUser;
    }

//    @PostMapping(path = "/registration", consumes = KekMediaType.REGISTRATION_USER, produces = KekMediaType.REGISTRATION_USER)
//    public void userRegistration(@RequestBody @Valid RegistrationDto userData, HttpServletResponse response) throws Exception {
//        logger.info("Created request for user registration: {}", userData);
//
//        final IUser user = createUser.createNewUser(userData);
//
//
//        final String redirectUrl = authenticationService.authenticateKekUser(user);
//
//        logger.debug("redirecting after authentication = {}", redirectUrl);
//        response.sendRedirect(redirectUrl);
//    }


    @PostMapping(path = "/registration", consumes = KekMediaType.REGISTRATION_USER, produces = KekMediaType.SESSION)
    public ResponseEntity<SessionDto> userRegistration(@RequestBody @Valid RegistrationDto userData,
                                                       HttpServletResponse response,
                                                       HttpSession session) throws Exception {
        logger.info("Created request for user registration: {}", userData);

        final IUser user = createUser.createNewUser(userData);


        final String redirectUrl = authenticationService.authenticateKekUser(user);

        logger.debug("redirecting after authentication = {}", redirectUrl);
        response.sendRedirect(redirectUrl);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setSessionId(session.getId());
        System.out.println("!!!!!!!!!!!!!!!! session: " + session.getId());
        return ResponseEntity.ok(sessionDto);
    }
}
