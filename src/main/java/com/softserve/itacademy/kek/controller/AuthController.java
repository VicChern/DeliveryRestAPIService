package com.softserve.itacademy.kek.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.auth0.AuthenticationController;
import com.auth0.Tokens;
import com.auth0.jwt.JWT;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.security.TokenAuthentication;
import com.softserve.itacademy.kek.security.TokenUtils;
import com.softserve.itacademy.kek.services.impl.UserDetailsServiceImpl;

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

    @Value(value = "${redirect.after.success.logout}")
    private String redirectAfterSuccessLogout;

    @GetMapping(path = "/login")
    protected void login(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Performing login, request = {}", request);

        final String returnTo = createRedirectUrl(request.getScheme(), request.getServerName(),
                request.getServerPort(), redirectAfterSuccessLogout);

        final String authorizeUrl = controller.buildAuthorizeUrl(request, response, returnTo)
                .withScope("openid profile email")
                .build();

        try {
            logger.debug("trying to redirect to authorizeUrl = {}", authorizeUrl);
            response.sendRedirect(authorizeUrl);

        } catch (IOException e) {
            logger.error("Failed to redirect to authorizeUrl {}, {}", authorizeUrl, e);
        }
    }

    @RequestMapping(path = "/callback")
    protected void getCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("Entered to getCallBack method req = {}", request);

        try {
            final Tokens tokens = controller.handle(request, response);
            final TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(tokens.getIdToken()));

            final UserDetailsService userDetailsService = new UserDetailsServiceImpl();
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

    @GetMapping(path = "/profile")
    protected ResponseEntity<String> profile(Authentication authentication) {

        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;

        JSONObject json = new JSONObject();
        json.put("profileJson", TokenUtils.claimsAsJson(tokenAuthentication.getClaims()));
        return ResponseEntity.ok(json.toString());
    }


    private String createRedirectUrl(String scheme, String serverName, int serverPort, String afterSuccessfulRedirect) {
        String returnTo = scheme + "://" + serverName;

        if ((scheme.equals("http") && serverPort != 80) ||
                (scheme.equals("https") && serverPort != 443)) {
            returnTo += ":" + serverPort;
        }

        returnTo += afterSuccessfulRedirect;

        return returnTo;
    }


}
