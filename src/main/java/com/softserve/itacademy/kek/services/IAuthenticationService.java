package com.softserve.itacademy.kek.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.softserve.itacademy.kek.models.IUser;

/**
 * Service interface for user authentication
 */
public interface IAuthenticationService {

    /**
     * Creates redirect URL to Auth0
     *
     * @param request  request
     * @param response response
     * @return redirect URL
     */
    String createRedirectUrl(HttpServletRequest request, HttpServletResponse response);

    /**
     * Sets authentication information for a user authenticated using Auth0
     *
     * @param request  request
     * @param response response
     * @return success authentication URL
     * @throws IOException if an error occurred
     */
    String authenticateAuth0User(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * Sets authentication information for a user authenticated using name/password
     *
     * @param user user data
     * @return success authentication URL
     */
    String authenticateKekUser(IUser user);
}
