package com.softserve.itacademy.kek.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.auth0.AuthenticationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/login")
@PropertySource("classpath:server.properties")
public class LoginController extends DefaultController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AuthenticationController controller;
    @Value(value = "${redirect.from.auth0}")
    private String redirectAuth0URL;

    @GetMapping
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

}
