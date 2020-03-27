package com.softserve.itacademy.kek.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.dto.SignInDto;
import com.softserve.itacademy.kek.exception.NoSuchUserException;
import com.softserve.itacademy.kek.models.enums.IdentityTypeDef;
import com.softserve.itacademy.kek.models.impl.Identity;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.IdentityRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IAuthenticationService;
import com.softserve.itacademy.kek.services.IGetTokenService;
import com.softserve.itacademy.kek.services.IIdentityService;

@RestController
public class SignInController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserRepository userRepository;
    private IIdentityService service;
    private IdentityRepository repository;
    private PasswordEncoder passwordEncoder;
    private IAuthenticationService authenticationService;
    private IGetTokenService getTokenService;

    @Autowired
    public SignInController(UserRepository userRepository, IIdentityService service,
                            IdentityRepository repository, PasswordEncoder passwordEncoder,
                            IAuthenticationService authenticationService, IGetTokenService getTokenService) {
        this.userRepository = userRepository;
        this.service = service;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.getTokenService = getTokenService;
    }

    @GetMapping(path = "/signin", consumes = "application/vnd.softserve.signin+json",
            produces = "application/vnd.softserve.signin+json")
    public void signIn(@RequestBody @Valid SignInDto dto, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        final User user;
        final Identity identity;

        try {
           user = userRepository.findByEmail(dto.getEmail());
        } catch (DataAccessException ex) {
           throw new NoSuchUserException("There is no user with this email", ex);
        }

        identity = (Identity) service.read(user.getGuid(), IdentityTypeDef.KEY);

        boolean isCorrect = passwordEncoder.matches(dto.getPassword(), identity.getPayload());

        if ( isCorrect ) {
            authenticationService.authenticateKekUser(user);

            Cookie cookie = new Cookie("token", "");

            cookie.setValue(getTokenService.getToken(user.getEmail()));
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setDomain("/");

            response.addCookie(cookie);

        } else {

            /**
             * etosoriystystdhgfdkj
             */
            throw new Exception();
        }

    }

}
