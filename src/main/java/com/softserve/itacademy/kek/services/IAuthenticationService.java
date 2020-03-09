package com.softserve.itacademy.kek.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.softserve.itacademy.kek.models.IUser;

public interface IAuthenticationService {

    String createRedirectUrl(HttpServletRequest request, HttpServletResponse response);

    String authenticateAuth0User(HttpServletRequest request, HttpServletResponse response) throws IOException;

    String authenticateKekUser(IUser user);
}
