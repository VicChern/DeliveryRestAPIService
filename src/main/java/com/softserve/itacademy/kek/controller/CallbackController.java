package com.softserve.itacademy.kek.controller;

import com.softserve.itacademy.kek.security.TokenAuthentication;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import com.auth0.jwt.JWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(path = "/callback")
@PropertySource("classpath:server.properties")
public class CallbackController extends DefaultController{

    @Autowired
    private AuthenticationController controller;

    @Value(value = "${redirect.on.fail}")
    private String redirectOnFail;
    @Value(value = "${redirect.on.success}")
    private String redirectOnSuccess;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping
    protected void getCallback(HttpServletRequest req, HttpServletResponse res) throws IOException {
        handle(req, res);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            logger.info("Authentication");

            Tokens tokens = controller.handle(request, response);
            TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(tokens.getIdToken()));
            SecurityContextHolder.getContext().setAuthentication(tokenAuth);

            logger.info("User was authenticated");

            response.sendRedirect(redirectOnSuccess);
        } catch (AuthenticationException | IdentityVerificationException e) {
            logger.error(e.getMessage());

            SecurityContextHolder.clearContext();
            response.sendRedirect(redirectOnFail);
        }
    }

}
