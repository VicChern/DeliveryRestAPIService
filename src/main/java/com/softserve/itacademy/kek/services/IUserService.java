package com.softserve.itacademy.kek.services;

import java.util.UUID;

import com.softserve.itacademy.kek.modelInterfaces.IUser;

/**
 * Service for work with user
 */
public interface IUserService {
    /**
     * Inserts new user to db
     *
     * @param userData user data
     * @return inserted user data
     */
    IUser create(IUser userData);

    /**
     * Updates user
     *
     * @param userData user data
     * @return updated user data
     */
    IUser update(IUser userData);

    /**
     * Deletes user in DB by user guid
     *
     * @param guid user guid
     */
    void delete(UUID guid);

    /**
     * Returns user data by user guid
     *
     * @param guid user guid
     * @return user data
     */
    IUser getByGuid(UUID guid);

    /**
     * Returns all users
     *
     * @return all users
     */
    Iterable<IUser> getAll();
}
