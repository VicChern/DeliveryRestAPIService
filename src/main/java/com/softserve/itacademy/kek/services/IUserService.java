package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.modelInterfaces.IUserData;

import java.util.UUID;

/**
 * Service for work with user
 */
public interface IUserService {
    /**
     * Inserts new user to db
     * @param userData user data
     * @return inserted user data
     */
    IUserData insert(IUserData userData);

    /**
     * Updates user
     * @param userData user data
     * @return updated user data
     */
    IUserData update(IUserData userData);

    /**
     * Deletes user in DB by user guid
     * @param guid user guid
     */
    void deleteByGuid(UUID guid);

    /**
     * Returns user data by user guid
     * @param guid user guid
     * @return user data
     */
    IUserData getByGuid(UUID guid);
}
