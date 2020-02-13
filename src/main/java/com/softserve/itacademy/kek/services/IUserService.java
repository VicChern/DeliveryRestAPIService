package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.modelInterfaces.IUser;

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
    IUser insert(IUser userData);

    /**
     * Updates user
     * @param userData user data
     * @return updated user data
     */
    IUser update(IUser userData);

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
    IUser getByGuid(UUID guid);
}
