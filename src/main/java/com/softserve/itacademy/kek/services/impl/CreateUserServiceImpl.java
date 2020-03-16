package com.softserve.itacademy.kek.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.dto.RegistrationDto;
import com.softserve.itacademy.kek.exception.UserAlreadyExistException;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.ICreateUserService;
import com.softserve.itacademy.kek.services.IIdentityService;
import com.softserve.itacademy.kek.services.IUserService;

@Service
public class CreateUserServiceImpl implements ICreateUserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IUserService userService;
    private final IIdentityService identityService;


    @Autowired
    public CreateUserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, IUserService userService, IIdentityService identityService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.identityService = identityService;
    }


    @Transactional
    @Override
    public IUser createNewUser(RegistrationDto userData) {
        final boolean isAlreadyRegistered = userRepository.findByEmail(userData.getEmail()) != null;
        if (isAlreadyRegistered) {
            logger.info("User already exists in DB {}", userData);
            throw new UserAlreadyExistException(userData.toString());
        }

        final User user = new User();

        user.setName(userData.getName());
        user.setNickname(userData.getNickname());
        user.setEmail(userData.getEmail());
        user.setPhoneNumber(userData.getPhoneNumber());

        final IUser dbUser = userService.create(user);

        identityService.savePassword((User) dbUser, passwordEncoder.encode(userData.getPassword()));

        logger.info("User has been added to DB {}", userData);
        return dbUser;
    }
}
