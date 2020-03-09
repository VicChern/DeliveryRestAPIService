package com.softserve.itacademy.kek.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IAuthenticationService {

    String createRedirectUrl(HttpServletRequest request, HttpServletResponse response);

    void authenticateUser(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
