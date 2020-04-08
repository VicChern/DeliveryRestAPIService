package com.softserve.itacademy.kek.services.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.auth0.AuthenticationController;
import com.auth0.Tokens;
import com.auth0.jwt.JWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.AuthenticationServiceException;
import com.softserve.itacademy.kek.models.enums.IdentityTypeEnum;
import com.softserve.itacademy.kek.models.impl.Identity;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.IdentityRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.security.TokenAuthentication;
import com.softserve.itacademy.kek.services.AbstractService;
import com.softserve.itacademy.kek.services.IAuthenticationService;

@Service
@PropertySource("classpath:server.properties")
public class AuthenticationServiceImpl extends AbstractService implements IAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Value(value = "${redirect.from.auth0}")
    private String redirectAuth0URL;

    @Value(value = "${redirect.on.fail}")
    private String redirectOnFail;

    @Value(value = "${redirect.on.success}")
    private String redirectOnSuccess;

    private AuthenticationController controller;
    private UserDetailsService userDetailsService;
    private UserRepository userRepository;
    private IdentityRepository identityRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationController controller,
                                     UserDetailsService userDetailsService,
                                     UserRepository userRepository,
                                     IdentityRepository identityRepository,
                                     PasswordEncoder passwordEncoder) {
        this.controller = controller;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.identityRepository = identityRepository;
        this.passwordEncoder = passwordEncoder;
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
    public String authenticateAuth0User(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Set authentication info (Auth0)");

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

    @Transactional(readOnly = true)
    @Override
    public void authenticateKekUser(String email, String key) throws AuthenticationServiceException {
        logger.info("Set authentication info: {}", email);

        validateUser(email, key);

        logger.debug("User is valid: {}", email);

        setUsernamePasswordAuthentication(email);

        logger.info("User was authenticated successfully: {}", email);
    }

    private void validateUser(String email, String key) {
        final Optional<User> user;
        String payload = "";

        try {
            user = userRepository.findByEmail(email);

            logger.debug("User was read from DB by email: {}", email);

            if (user.isPresent()) {
                final UUID guid = user.get().getGuid();
                final String type = IdentityTypeEnum.KEY.toString();

                final Identity identity = identityRepository.findByUserGuidAndIdentityTypeName(guid, type).orElseThrow();

                logger.debug("User identity was read from DB");

                payload = identity.getPayload();

                if (payload == null || payload.isEmpty()) {
                    logger.error("User identity is not valid");
                    throw new NoSuchElementException("User identity is empty");
                }
            }
        } catch (Exception ex) {
            logger.error("Error while authenticate user", ex);
            throw new AuthenticationServiceException("An error occurred while authenticate user", ex);
        }

        boolean valid = user.isPresent();
        if (valid) {
            logger.debug("User was found: {}", email);

            valid = passwordEncoder.matches(key, payload);

            if (valid) {
                logger.debug("Password is valid for user {}", email);
            }
        }

        if (!valid) {
            logger.error("User is not valid: {}", email);
            throw new AuthenticationServiceException("Invalid email or password", HttpStatus.FORBIDDEN.value());
        }
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
