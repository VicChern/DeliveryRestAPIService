package com.softserve.itacademy.kek.services;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;

import com.softserve.itacademy.kek.exception.UserServiceException;
import com.softserve.itacademy.kek.models.IUser;

/**
 * Service interface for users
 */
public interface IUserService {
    /**
     * Inserts new user to db
     *
     * @param user user data
     * @return inserted user data
     * @throws UserServiceException if an error occurred
     */
    IUser create(IUser user) throws UserServiceException;

    /**
     * Updates user
     *
     * @param user user data
     * @return updated user data
     * @throws UserServiceException if an error occurred
     */
    IUser update(IUser user) throws UserServiceException;

    /**
     * Deletes user in DB by user guid
     *
     * @param guid user guid
     * @throws UserServiceException if an error occurred
     */
    void deleteByGuid(UUID guid) throws UserServiceException;

    /**
     * Deletes all users except admin in DB
     *
     * @throws UserServiceException if an error occurred
     */
    // TODO: 09.04.2020 not working
    void deleteAll() throws UserServiceException;

    /**
     * Returns user data by user guid
     *
     * @param guid user guid
     * @return user data
     * @throws UserServiceException if an error occurred
     */
    IUser getByGuid(UUID guid) throws UserServiceException;

    /**
     * Returns user data by user email
     *
     * @param email user guid
     * @return user data
     * @throws UserServiceException if an error occurred
     */
    IUser getByEmail(String email) throws UserServiceException;

    /**
     * Returns all users
     *
     * @return all users
     * @throws UserServiceException if an error occurred
     */
    List<IUser> getAll() throws UserServiceException;

    /**
     * Returns a {@link Page} of {@link IUser} meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable {@code Pageable} definition of page options, must not be {@literal null}.
     * @return a page of {@link IUser}
     * @throws UserServiceException if an error occurred
     */
    Page<IUser> getAll(Pageable pageable) throws UserServiceException;

    /**
     * Returns user roles
     *
     * @param email user email
     * @return list of user roles
     * @throws UserServiceException if an error occurred
     */
    Collection<? extends GrantedAuthority> getUserAuthorities(String email) throws UserServiceException;

}
