package com.softserve.itacademy.kek.modelInterfaces;

import java.util.UUID;

/**
 * Interface for User data exchange with Service Layer
 */
public interface IUser {
    /**
     * Returns user GUID
     *
     * @return user GUID
     */
    UUID getGuid();

    /**
     * Returns user name
     *
     * @return user name
     */
    String getName();

    /**
     * Returns user nickname
     *
     * @return user nickname
     */
    String getNickname();

    /**
     * Returns user email
     *
     * @return user email
     */
    String getEmail();

    /**
     * Returns user phone number
     *
     * @return user phone number
     */
    String getPhoneNumber();

    /**
     * Returns user details
     * @return user details
     */
    IUserDetails getUserDetails();
}
