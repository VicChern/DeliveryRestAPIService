package com.softserve.itacademy.kek.services;


public interface IGetTokenService {

    /**
     * @param email email
     * @return A token
     */
    String getToken(String email);
}
