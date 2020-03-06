package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.softserve.itacademy.kek.exception.UserServiceException;
import com.softserve.itacademy.kek.models.IUser;

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
    IUser create(IUser userData) throws UserServiceException;

    /**
     * Updates user
     *
     * @param userData user data
     * @return updated user data
     */
    IUser update(IUser userData) throws UserServiceException;

    /**
     * Deletes user in DB by user guid
     *
     * @param guid user guid
     */
    void deleteByGuid(UUID guid) throws UserServiceException;

    /**
     * Returns user data by user guid
     *
     * @param guid user guid
     * @return user data
     */
    IUser getByGuid(UUID guid) throws UserServiceException;

    /**
     * Returns all users
     *
     * @return all users
     */
    List<IUser> getAll() throws UserServiceException;

    /**
     * Returns a {@link Page} of {@link IUser} meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable {@code Pageable} definition of page options, must not be {@literal null}.
     * @return a page of {@link IUser}
     */
    Page<IUser> getAll(Pageable pageable) throws UserServiceException;
}
