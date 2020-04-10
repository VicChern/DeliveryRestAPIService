package com.softserve.itacademy.kek.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.controller.utils.KekMappingValues;
import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.dto.TokenDto;
import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.mappers.IUserMapper;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.services.IAuthenticationService;
import com.softserve.itacademy.kek.services.ITokenService;
import com.softserve.itacademy.kek.services.IUserService;

@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthController extends DefaultController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IAuthenticationService authenticationService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITokenService tokenService;

    @GetMapping(path = "/login")
    protected void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("Performing login: request = {}", request);

        final String authorizeUrl = authenticationService.createRedirectUrl(request, response);

        logger.debug("Redirect to authorize URL: {}", authorizeUrl);
        response.sendRedirect(authorizeUrl);
    }

    @RequestMapping(path = "/callback")
    protected ResponseEntity<TokenDto> getCallback(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Entered to callback for Auth0: request = {}", request);

        final String email = authenticationService.authenticateAuth0User(request, response);

        logger.debug("User was authenticated successfully: {}", email);

        final String token = tokenService.getToken(email);
        final TokenDto tokenDto = new TokenDto(token);

        logger.info("Sending response to the client after successful authentication: {}", email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tokenDto);
    }

    @GetMapping(path = KekMappingValues.PROFILE, produces = KekMediaType.USER)
    @PreAuthorize("hasRole('TENANT') or hasRole('USER') or hasRole('ACTOR')")
    protected ResponseEntity<UserDto> profile() {
        final String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("Performing profile request for: {}", email);

        final IUser user = userService.getByEmail(email);

        UserDto userDto = IUserMapper.INSTANCE.toUserDto(user);

        logger.debug("Performed profile request : {}", userDto);
        return ResponseEntity.ok(userDto);
    }

}