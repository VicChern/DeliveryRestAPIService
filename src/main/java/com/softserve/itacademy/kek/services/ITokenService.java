package com.softserve.itacademy.kek.services;

/**
 * Service interface for tokens
 */
public interface ITokenService {

    /**
     * Returns token
     *
     * @param email user email
     * @return token
     */
    String getToken(String email);
}
