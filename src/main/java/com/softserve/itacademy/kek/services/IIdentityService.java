package com.softserve.itacademy.kek.services;


import java.util.UUID;

import com.softserve.itacademy.kek.models.IIdentity;
import com.softserve.itacademy.kek.models.IUser;
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
    IIdentity create(IUser userData, String password);

    /**
     * Get user password from db
     *
     * @param email
     * @return user password
     */
    IIdentity read(String email);

    /**
     * Set new user password
     *
     * @param user
     * @param password
     * @return user password
     */
    IIdentity update(IUser user, String password);

    /**
     * Delete user password
     *
     * @param identity
     */
    void delete(IIdentity identity);



}
