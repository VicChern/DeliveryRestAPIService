package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.dto.RegistrationDto;
import com.softserve.itacademy.kek.models.IUser;

/**
 * Service for user creation
 */
public interface ICreateUserService {

    /**
     * Creates a new User
     * @param userData userData
     * @return created user
     */
    IUser createNewUser(RegistrationDto userData);
}
