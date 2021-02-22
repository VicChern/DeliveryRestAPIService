package com.softserve.itacademy.kek.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.softserve.itacademy.kek.exception.AuthenticationServiceException;

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
     * @return user email
     * @throws AuthenticationServiceException if an error occurred
     */
    String authenticateAuth0User(HttpServletRequest request, HttpServletResponse response) throws AuthenticationServiceException;

    /**
     * Sets authentication information for a user authenticated using name/password
     *
     * @param email user email
     * @param key   user key
     * @throws AuthenticationServiceException if an error occurred
     */
    void authenticateKekUser(String email, String key) throws AuthenticationServiceException;
}
