package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.dto.RegistrationDto;
import com.softserve.itacademy.kek.exception.CreateUserServiceException;
import com.softserve.itacademy.kek.models.IUser;

/**
 * Service interface to register user
 */
public interface ICreateUserService {

    /**
     * Registers user in system
     *
     * @param userData user data
     * @return created user
     * @throws CreateUserServiceException if an error occurred
     */
    IUser createNewUser(RegistrationDto userData) throws CreateUserServiceException;
}
