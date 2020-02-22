package com.softserve.itacademy.kek.controller;

import com.auth0.AuthenticationController;
import com.auth0.Tokens;
import com.auth0.jwt.JWT;
import com.softserve.itacademy.kek.security.TokenAuthentication;
import com.softserve.itacademy.kek.security.TokenUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@PropertySource("classpath:server.properties")
public class AuthController extends DefaultController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthenticationController controller;

    @Value(value = "${redirect.from.auth0}")
    private String redirectAuth0URL;

    @Value(value = "${redirect.on.fail}")
    private String redirectOnFail;

    @Value(value = "${redirect.on.success}")
    private String redirectOnSuccess;


    @GetMapping(path = "/login")
    protected void login(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Performing login");

        String redirectUri = request.getScheme() + "://" + request.getServerName();

        if ((request.getScheme().equals("http") && request.getServerPort() != 80) ||
                (request.getScheme().equals("https") && request.getServerPort() != 443)) {

            redirectUri += ":" + request.getServerPort();

        }

        redirectUri += redirectAuth0URL;

        final String authorizeUrl = controller.buildAuthorizeUrl(request, response, redirectUri)
                .withScope("openid profile email")
                .build();

        try {
            logger.info("trying to redirect to authorizeUrl {}", authorizeUrl);
            response.sendRedirect(authorizeUrl);

        } catch (IOException e) {
            logger.error("Failed to redirect to authorizeUrl - " + authorizeUrl, e);
        }
    }

    @RequestMapping(path = "/callback")
    protected void getCallback(HttpServletRequest req, HttpServletResponse res) throws IOException {
        logger.info("Entered to getCallBack method");
        handle(req, res);
    }


    private void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("entered to handle method");
        try {
            logger.info("Authentication");

            final Tokens tokens = controller.handle(request, response);
            final TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(tokens.getIdToken()));
            SecurityContextHolder.getContext().setAuthentication(tokenAuth);

            logger.info("User was authenticated");

            response.sendRedirect(redirectOnSuccess);
        } catch (Exception e) {
            logger.error("Error while authentication", e);

            SecurityContextHolder.clearContext();
            response.sendRedirect(redirectOnFail);
        }
    }

    @GetMapping(path = "/profile")
    protected ResponseEntity<String> profile(Authentication authentication) {

        final TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;

        final JSONObject json = new JSONObject();
        json.put("profileJson", TokenUtils.claimsAsJson(tokenAuthentication.getClaims()));
        return ResponseEntity.ok(json.toString());
    }

}
