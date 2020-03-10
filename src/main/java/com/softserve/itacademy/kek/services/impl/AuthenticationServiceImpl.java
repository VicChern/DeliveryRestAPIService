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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

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

    private AuthenticationController controller;
    private UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationController controller, UserDetailsService userDetailsService) {
        this.controller = controller;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String createRedirectUrl(HttpServletRequest request, HttpServletResponse response) {
        final String returnTo = createRedirectUrl(request.getScheme(), request.getServerName(),
                request.getServerPort());

        final String authorizeUrl = controller.buildAuthorizeUrl(request, response, returnTo)
                .withScope("openid profile email")
                .build();

        return authorizeUrl;
    }

    @Override
    public void authenticateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            final Tokens tokens = controller.handle(request, response);
            final TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(tokens.getIdToken()));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(tokenAuth.getClaims().get("email").asString());

            final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            logger.info("User was authenticated successfully");

            response.sendRedirect(redirectOnSuccess);
        } catch (Exception e) {
            logger.error("Error while authentication", e);

            SecurityContextHolder.clearContext();
            response.sendRedirect(redirectOnFail);
        }
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
