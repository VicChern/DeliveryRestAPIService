package com.softserve.itacademy.kek.services.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.auth0.AuthenticationController;
import com.auth0.Tokens;
import com.auth0.jwt.JWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.security.TokenAuthentication;
import com.softserve.itacademy.kek.services.IAuthenticationService;

@Service
@PropertySource("classpath:server.properties")
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value(value = "${redirect.from.auth0}")
    private String redirectAuth0URL;

    @Value(value = "${redirect.on.fail}")
    private String redirectOnFail;

    @Value(value = "${redirect.on.success}")
    private String redirectOnSuccess;

    private final AuthenticationController controller;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationController controller, UserDetailsService userDetailsService) {
        this.controller = controller;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String createRedirectUrl(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Create redirect URL");

        final String returnTo = createRedirectUrl(request.getScheme(), request.getServerName(),
                request.getServerPort());

        final String authorizeUrl = controller.buildAuthorizeUrl(request, response, returnTo)
                .withScope("openid profile email")
                .build();

        return authorizeUrl;
    }

    @Override
    public String authenticateAuth0User(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("User authentication");

        try {
            final Tokens tokens = controller.handle(request, response);
            final TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(tokens.getIdToken()));

            final String email = tokenAuth.getClaims().get("email").asString();

            setUsernamePasswordAuthentication(email);
            logger.info("User was authenticated successfully, redirectUrl - {}", redirectOnSuccess);

            return redirectOnSuccess;
        } catch (Exception e) {
            logger.error("Error while authentication, redirectUrl - " + redirectOnFail + ", error - " + e);

            SecurityContextHolder.clearContext();
            return redirectOnFail;
        }
    }

    @Override
    public String authenticateKekUser(IUser user) {
        logger.info("User authentication");

        setUsernamePasswordAuthentication(user.getEmail());

        logger.info("User was authenticated successfully, redirectUrl - {}", redirectOnSuccess);
        return redirectOnSuccess;
    }

    private void setUsernamePasswordAuthentication(String email) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                email, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private String createRedirectUrl(String scheme, String serverName, int serverPort) {
        String returnTo = scheme + "://" + serverName;

        if ((scheme.equals("http") && serverPort != 80) ||
                (scheme.equals("https") && serverPort != 443)) {
            returnTo += ":" + serverPort;
        }

        returnTo += redirectAuth0URL;

        return returnTo;
    }
}
