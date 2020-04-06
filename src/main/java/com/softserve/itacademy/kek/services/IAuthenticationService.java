package com.softserve.itacademy.kek.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.softserve.itacademy.kek.models.IUser;

/**
 * Service for Authentication
 */
public interface IAuthenticationService {

    /**
     * Creates url for redirecting after request
     *
     * @param request  request
     * @param response request
     * @return string with url
     */
    String createRedirectUrl(HttpServletRequest request, HttpServletResponse response);

    /**
     * Creates request and returns a string with user
     *
     * @param request  request
     * @param response response
     * @return returns string with user
     * @throws IOException IOException
     */
    String authenticateAuth0User(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * Creates request and returns a string with User
     *
     * @param user user
     * @return return response of user authentication
     */
    String authenticateKekUser(IUser user);
}
