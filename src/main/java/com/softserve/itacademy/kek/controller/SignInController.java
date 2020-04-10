package com.softserve.itacademy.kek.controller;

import javax.servlet.http.HttpServletRequest;
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

import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.controller.utils.KekPaths;
import com.softserve.itacademy.kek.dto.SignInDto;
import com.softserve.itacademy.kek.dto.TokenDto;
import com.softserve.itacademy.kek.services.IAuthenticationService;
import com.softserve.itacademy.kek.services.ITokenService;

@RestController
public class SignInController extends DefaultController {
    private static final Logger logger = LoggerFactory.getLogger(SignInController.class);

    private final IAuthenticationService authenticationService;
    private final ITokenService tokenService;

    @Autowired
    public SignInController(IAuthenticationService authenticationService, ITokenService getTokenService) {
        this.authenticationService = authenticationService;
        this.tokenService = getTokenService;
    }

    @PostMapping(path = KekPaths.SIGNIN, consumes = KekMediaType.SIGNIN,
            produces = KekMediaType.TOKEN)
    public ResponseEntity<TokenDto> signIn(@RequestBody @Valid SignInDto dto, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        logger.info("Try to sign in: {}", dto.getEmail());

        authenticationService.authenticateKekUser(dto.getEmail(), dto.getPassword());

        TokenDto token = new TokenDto(tokenService.getToken(dto.getEmail()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(token);
    }
}
