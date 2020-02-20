package com.softserve.itacademy.kek.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value(value = "${redirect.after.success.logout}")
    private String redirectAfterSuccessLogout;

    @Autowired
    private WebSecurityConfig webSecurityConfig;

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

        final String logoutUrl = String.format(
                "https://%s/v2/logout?client_id=%s&returnTo=%s",
                webSecurityConfig.getDomain(),
                webSecurityConfig.getClientId(),
                returnTo);

        try {
            logger.info("trying to redirect to logoutUrl - {}", logoutUrl);
            res.sendRedirect(logoutUrl);

        } catch (Exception e) {
            logger.error("Failed to redirect to logoutUrl - " + logoutUrl, e);
            throw new IllegalArgumentException(e);
        }
    }

    private void invalidateSession(HttpServletRequest request) {
        if (request.getSession() != null) {
            request.getSession().invalidate();
        }
    }

}
