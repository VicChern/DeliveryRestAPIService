package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.dto.RegistrationDto;

public interface ICreateUserService {

    boolean createNewUser(RegistrationDto userData);
}
