package com.softserve.itacademy.kek.services;


import com.softserve.itacademy.kek.models.impl.Identity;
import com.softserve.itacademy.kek.models.impl.User;

public interface IIdentityService {

    /**
     * Inserts new user password to db
     *
     * @param userData
     * @param password
     * @return inserted user data
     */
    Identity savePassword(User userData, String password);

}
