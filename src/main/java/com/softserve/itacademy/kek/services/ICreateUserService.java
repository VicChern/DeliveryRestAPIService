package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.dto.RegistrationDto;
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
     */
    IUser createNewUser(RegistrationDto userData);
}
