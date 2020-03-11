package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.dto.RegistrationDto;
import com.softserve.itacademy.kek.models.IUser;

public interface ICreateUserService {

    IUser createNewUser(RegistrationDto userData);
}
