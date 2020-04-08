package com.softserve.itacademy.kek.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.dto.RegistrationDto;
import com.softserve.itacademy.kek.exception.CreateUserServiceException;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.enums.IdentityTypeEnum;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.AbstractService;
import com.softserve.itacademy.kek.services.ICreateUserService;
import com.softserve.itacademy.kek.services.IIdentityService;
import com.softserve.itacademy.kek.services.IUserService;

@Service
public class CreateUserServiceImpl extends AbstractService implements ICreateUserService {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserServiceImpl.class);

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
    public IUser createNewUser(RegistrationDto userData) throws CreateUserServiceException {
        logger.info("User registration: email = {}", userData.getEmail());

        final Optional<User> userFromDb;
        IUser createdUser = null;

        try {
            userFromDb = userRepository.findByEmail(userData.getEmail());

            logger.debug("User was read from DB by email: {}", userData.getEmail());

            if (userFromDb.isEmpty()) {
                createdUser = userService.create(userData);

                logger.debug("User was inserted into DB: {}", createdUser);

                identityService.create(createdUser.getGuid(), IdentityTypeEnum.KEY,
                        passwordEncoder.encode(userData.getPassword()));

                logger.debug("Identity was inserted into DB for user: {}", createdUser.getEmail());
            }
        } catch (Exception ex) {
            logger.error("Error while register user", ex);
            throw new CreateUserServiceException("An error occurred while register user", ex);
        }

        if (userFromDb.isPresent()) {
            logger.error("User already exists in DB: email = {}", userData.getEmail());
            throw new CreateUserServiceException("User already exists", HttpStatus.FORBIDDEN.value());
        }

        logger.info("User was added into DB: {}", userData);

        return createdUser;
    }
}
