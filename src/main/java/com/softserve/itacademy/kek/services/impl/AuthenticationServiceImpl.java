package com.softserve.itacademy.kek.services.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.softserve.itacademy.kek.services.IAuthenticationService;
import com.softserve.itacademy.kek.services.IUserService;

@Service
@PropertySource("classpath:server.properties")
public class AuthenticationServiceImpl implements IAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Value(value = "${redirect.from.auth0}")
    private String redirectAuth0URL;

    private AuthenticationController authController;
    private IUserService userService;
    private UserRepository userRepository;
    private IdentityRepository identityRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationController controller,
                                     IUserService userService,
                                     UserRepository userRepository,
                                     IdentityRepository identityRepository,
                                     PasswordEncoder passwordEncoder) {
        this.authController = controller;
        this.userService = userService;
        this.userRepository = userRepository;
        this.identityRepository = identityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String createRedirectUrl(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Create redirect URL");

        final String returnTo = createRedirectUrl(request.getScheme(), request.getServerName(),
                request.getServerPort());

        final String authorizeUrl = authController.buildAuthorizeUrl(request, response, returnTo)
                .withScope("openid profile email")
                .build();

        return authorizeUrl;
    }

    @Transactional(readOnly = true)
    @Override
    public String authenticateAuth0User(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationServiceException {
        logger.info("Set authentication for Auth0 way");

        try {
            final Tokens tokens = authController.handle(request, response);
            final TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(tokens.getIdToken()));

            final String email = tokenAuth.getClaims().get("email").asString();

            userRepository.findByEmail(email).orElseThrow(() -> {
                logger.error("User authenticated by Auth0 was not found");
                return new AuthenticationServiceException("User was not found", HttpStatus.FORBIDDEN.value());
            });

            setAuthentication(email);

            logger.debug("Authentication was set: {}", email);

            return email;
        } catch (AuthenticationServiceException ex) {
            SecurityContextHolder.clearContext();
            throw ex;
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            logger.error("Error while setting authentication", ex);
            throw new AuthenticationServiceException("An error occurred while authentication", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void authenticateKekUser(String email, String key) throws AuthenticationServiceException {
        logger.info("Set authentication: {}", email);

        validateUser(email, key);

        logger.debug("User is valid: {}", email);

        try {
            setAuthentication(email);
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();

            logger.error("Error while authentication: " + email, ex);
            throw new AuthenticationServiceException("An error occurred while authentication", ex);
        }

        logger.info("User was authenticated successfully: {}", email);
    }

    private void validateUser(String email, String key) {
        final Optional<User> user = getUserByEmail(email);

        boolean valid = user.isPresent();
        if (valid) {
            logger.debug("User was found: {}", email);

            final String userKey = getUserKey(user.get().getGuid());

            valid = passwordEncoder.matches(key, userKey);

            if (valid) {
                logger.debug("Password is valid for user {}", email);
            }
        }

        if (!valid) {
            logger.error("User is not valid: {}", email);
            throw new AuthenticationServiceException("Invalid email or password", HttpStatus.FORBIDDEN.value());
        }
    }

    private void setAuthentication(String email) {
        Collection<? extends GrantedAuthority> authorities = userService.getAuthorities(email);

        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                email, null, authorities);

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

    private Optional<User> getUserByEmail(String email) {
        logger.info("Get user from DB by email: {}", email);

        try {
            final Optional<User> user = userRepository.findByEmail(email);

            logger.debug("User was read from DB by email: {}", email);

            return user;
        } catch (Exception ex) {
            logger.error("Error while authenticate user", ex);
            throw new AuthenticationServiceException("An error occurred while authenticate user", ex);
        }
    }

    private String getUserKey(UUID userGuid) {
        logger.info("Get user identity: userGuid = {}", userGuid);

        try {
            final String type = IdentityTypeEnum.KEY.toString();

            final Identity identity = identityRepository.findByUserGuidAndIdentityTypeName(userGuid, type).orElseThrow();

            logger.debug("User identity was read from DB");

            final String payload = identity.getPayload();

            if (payload == null || payload.isEmpty()) {
                logger.error("User identity is not valid");
                throw new NoSuchElementException("User identity is empty");
            }

            return payload;
        } catch (Exception ex) {
            logger.error("Error while authenticate user", ex);
            throw new AuthenticationServiceException("An error occurred while authenticate user", ex);
        }
    }
}
