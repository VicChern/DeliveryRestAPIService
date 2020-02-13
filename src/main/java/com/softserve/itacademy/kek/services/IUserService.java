package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.modelInterfaces.IUser;

import java.util.UUID;

/**
 * Service for work with user
 */
public interface IUserService {
    /**
     * Inserts new user to db
     * @param user user data
     * @return inserted user data
     */
    IUser create(IUser user);

    /**
     * Updates user
     * @param user user data
     * @return updated user data
     */
    IUser update(IUser user);

    /**
     * Deletes user in DB by user guid
     * @param guid user guid
     */
    void delete(UUID guid);

    /**
     * Returns user data by user guid
     * @param guid user guid
     * @return user data
     */
    IUser getByGuid(UUID guid);
}
