package com.softserve.itacademy.kek.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.controller.utils.KekMappingValues;
import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.dto.SignInDto;
import com.softserve.itacademy.kek.dto.TokenDto;
import com.softserve.itacademy.kek.exception.InvalidCredentialsException;
import com.softserve.itacademy.kek.models.enums.IdentityTypeEnum;
import com.softserve.itacademy.kek.models.impl.Identity;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.services.IAuthenticationService;
import com.softserve.itacademy.kek.services.IGetTokenService;
import com.softserve.itacademy.kek.services.IIdentityService;
import com.softserve.itacademy.kek.services.IUserService;

@RestController
public class SignInController {
    private static final Logger logger = LoggerFactory.getLogger(SignInController.class);

    private IUserService userService;
    private IIdentityService iIdentityService;
    private PasswordEncoder passwordEncoder;
    private IAuthenticationService authenticationService;
    private IGetTokenService getTokenService;

    @Autowired
    public SignInController(IUserService userService, IIdentityService iIdentityService,
                            PasswordEncoder passwordEncoder, IAuthenticationService authenticationService,
                            IGetTokenService getTokenService) {
        this.userService = userService;
        this.iIdentityService = iIdentityService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.getTokenService = getTokenService;
    }

    @PostMapping(path = KekMappingValues.SIGNIN, consumes = KekMediaType.SIGNIN,
            produces = KekMediaType.TOKEN)
    public ResponseEntity<TokenDto> signIn(@RequestBody @Valid SignInDto dto, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        logger.info("{} trying to sign in", dto.getEmail());
        final User user;
        final Identity identity;

        user = (User) userService.getByEmail(dto.getEmail());
        identity = (Identity) iIdentityService.get(user.getGuid(), IdentityTypeEnum.KEY);

        boolean isCorrect = passwordEncoder.matches(dto.getPassword(), identity.getPayload());

        if (isCorrect) {
            logger.info("Password is correct. Starting user authentication: {}", user);

            authenticationService.authenticateKekUser(user);

            TokenDto token = new TokenDto(getTokenService.getToken(user.getEmail()));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(token);

        } else {
            logger.error("Invalid password. Login attempt for user: {}", user);
            throw new InvalidCredentialsException("Invalid credentials. Try again.");
        }
    }
}
