package com.softserve.itacademy.kek.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

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
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.security.TokenAuthentication;
import com.softserve.itacademy.kek.security.WebSecurityConfig;
import com.softserve.itacademy.kek.services.impl.UserDetailsServiceImpl;

@RestController
@PropertySource("classpath:server.properties")
public class AuthController extends DefaultController implements LogoutSuccessHandler {
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

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    @GetMapping(path = "/login")
    protected void login(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Performing login");

        String redirectUri = request.getScheme() + "://" + request.getServerName();

        if ((request.getScheme().equals("http") && request.getServerPort() != 80) ||
                (request.getScheme().equals("https") && request.getServerPort() != 443)) {

            redirectUri += ":" + request.getServerPort();

        }

        redirectUri += redirectAuth0URL;

        String authorizeUrl = controller.buildAuthorizeUrl(request, response, redirectUri)
                .withScope("openid profile email")
                .build();

        try {
            logger.info("trying to redirect to authorizeUrl");
            response.sendRedirect(authorizeUrl);

        } catch (IOException e) {
            logger.error("Failed to redirect to authorizeUrl {}, {}", authorizeUrl, e);
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

            final UserDetailsService userDetailsService = new UserDetailsServiceImpl();
            final UserDetails userDetails = userDetailsService.loadUserByUsername(tokenAuth.getClaims().get("email").asString());

            final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

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

        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;

        JSONObject json = new JSONObject();
        json.put("profileJson", TokenUtils.claimsAsJson(tokenAuthentication.getClaims()));
        return ResponseEntity.ok(json.toString());
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) {
        logger.debug("Performing logout");

        invalidateSession(req);

        String returnTo = req.getScheme() + "://" + req.getServerName();

        if ((req.getScheme().equals("http") && req.getServerPort() != 80) ||
                (req.getScheme().equals("https") && req.getServerPort() != 443)) {
            returnTo += ":" + req.getServerPort();
        }

        returnTo += redirectAfterSuccessLogout;

        String logoutUrl = String.format(
                "https://%s/v2/logout?client_id=%s&returnTo=%s",
                webSecurityConfig.getDomain(),
                webSecurityConfig.getClientId(),
                returnTo);

        try {
            logger.info("trying to redirect to logoutUrl");
            res.sendRedirect(logoutUrl);

        } catch (Exception e) {
            logger.error("Failed to redirect to logoutUrl {}, {}", logoutUrl, e);
            throw new IllegalArgumentException(e);
        }
    }

    private void invalidateSession(HttpServletRequest request) {
        if (request.getSession() != null) {
            request.getSession().invalidate();
        }
    }


}
