package com.softserve.itacademy.kek.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.controller.utils.KekMappingValues;
import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.dto.SignInDto;
import com.softserve.itacademy.kek.exception.InvalidCredentialsException;
import com.softserve.itacademy.kek.exception.KekException;
import com.softserve.itacademy.kek.models.enums.IdentityTypeEnum;
import com.softserve.itacademy.kek.models.impl.Identity;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IAuthenticationService;
import com.softserve.itacademy.kek.services.IGetTokenService;
import com.softserve.itacademy.kek.services.IIdentityService;

@RestController
public class SignInController {
    private static final Logger logger = LoggerFactory.getLogger(SignInController.class);

    private UserRepository userRepository;
    private IIdentityService iIdentityService;
    private PasswordEncoder passwordEncoder;
    private IAuthenticationService authenticationService;
    private IGetTokenService getTokenService;

    @Autowired
    public SignInController(UserRepository userRepository, IIdentityService iIdentityService,
                            PasswordEncoder passwordEncoder, IAuthenticationService authenticationService,
                            IGetTokenService getTokenService) {
        this.userRepository = userRepository;
        this.iIdentityService = iIdentityService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.getTokenService = getTokenService;
    }

    @GetMapping(path = KekMappingValues.SIGNIN, consumes = KekMediaType.SIGNIN, produces = KekMediaType.SIGNIN)
    public ResponseEntity signIn(@RequestBody @Valid SignInDto dto, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        logger.info("Client requested login: {}", dto.getEmail());

        final Optional<User> userFromDb;
        try {
            userFromDb = userRepository.findByEmail(dto.getEmail());
        } catch (Exception ex) {
            logger.error("Error while getting user by email", ex);
            throw new KekException("An error occurs while signing in.", ex);
        }

        if (userFromDb.isEmpty()) {
            logger.error("User was not found in DB by email", new NoSuchElementException());
            throw new InvalidCredentialsException("Invalid credentials. Try again.");
        }

        logger.debug("User was found in DB by email: {}", dto.getEmail());

        final User user = userFromDb.get();

        final Identity identity = (Identity) iIdentityService.read(user.getGuid(), IdentityTypeEnum.KEY);

        boolean isCorrect = passwordEncoder.matches(dto.getPassword(), identity.getPayload());

        if (isCorrect) {
            logger.info("Password is correct. Starting user authentication: {}", user);

            authenticationService.authenticateKekUser(user);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(getTokenService.getToken(user.getEmail()));

        } else {
            logger.error("Invalid password login attempt for user: {}", user);
            throw new InvalidCredentialsException("Invalid credentials. Try again.");
        }
    }
}
