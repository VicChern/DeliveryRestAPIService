package com.softserve.itacademy.kek.services.impl;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.UserServiceException;
import com.softserve.itacademy.kek.modelInterfaces.IUser;
import com.softserve.itacademy.kek.modelInterfaces.IUserDetails;
import com.softserve.itacademy.kek.models.User;
import com.softserve.itacademy.kek.models.UserDetails;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.ITenantService;
import com.softserve.itacademy.kek.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {

    final Logger logger = LoggerFactory.getLogger(ITenantService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public IUser create(IUser user) {
        logger.info("Insert User into DB: {}", user);

        User actualUser = new User();

        actualUser.setGuid(UUID.randomUUID());
        actualUser.setName(user.getName());
        actualUser.setNickname(user.getNickname());
        actualUser.setEmail(user.getEmail());
        actualUser.setPhoneNumber(user.getPhoneNumber());

        UserDetails actualDetails = new UserDetails();

        IUserDetails details = user.getUserDetails();
        if (details != null) {
            actualDetails.setImageUrl(details.getImageUrl());
            actualDetails.setPayload(details.getPayload());
        }

        actualUser.setUserDetails(actualDetails);

        try {
            actualUser = userRepository.save(actualUser);
        } catch (PersistenceException | ConstraintViolationException ex) {
            logger.error("User wasn't inserted into DB: {}", actualUser, ex);
            throw new UserServiceException("User wasn't saved");
        }

        logger.info("User was inserted into DB: guid = {}", actualUser.getGuid());

        return actualUser;
    }

    @Transactional
    @Override
    public IUser update(IUser user) {
        logger.info("Update User in DB: {}", user);

        User actualUser = findActualUser(user.getGuid());
        actualUser.setName(user.getName());
        actualUser.setNickname(user.getNickname());
        actualUser.setEmail(user.getEmail());
        actualUser.setPhoneNumber(user.getPhoneNumber());

        UserDetails actualDetails = new UserDetails();
        actualDetails.setIdUser(actualUser.getIdUser());

        IUserDetails details = user.getUserDetails();
        if (details != null) {
            actualDetails.setImageUrl(details.getImageUrl());
            actualDetails.setPayload(details.getPayload());
        }

        actualUser.setUserDetails(actualDetails);

        try {
            actualUser = userRepository.save(actualUser);
        } catch (PersistenceException | ConstraintViolationException ex) {
            logger.error("User wasn't updated in DB: {}", actualUser);
            throw new UserServiceException("User wasn't saved");
        }

        logger.info("User was updated in DB: guid = {}", actualUser.getGuid());

        return actualUser;
    }

    @Transactional
    @Override
    public void delete(UUID guid) {
        logger.info("Delete User from DB: guid = {}", guid);

        User actualUser = findActualUser(guid);

        try {
            userRepository.deleteById(actualUser.getIdUser());
        } catch (PersistenceException ex) {
            logger.error("User wasn't deleted from DB: {}", actualUser);
            throw new UserServiceException("User wasn't deleted");
        }

        logger.info("User was deleted from DB: guid = {}", actualUser.getGuid());
    }

    @Override
    public IUser getByGuid(UUID guid) {
        return findActualUser(guid);
    }

    @Override
    public Iterable<IUser> getAll() {
        logger.info("Get all Users");
        Iterable<? extends IUser> users = userRepository.findAll();
        return (Iterable<IUser>) users;
    }

    @Transactional(readOnly = true)
    private User findActualUser(UUID guid) {
        logger.info("Find User in DB: guid = {}", guid);

        User user;
        try {
            user = userRepository.findByGuid(guid);
        } catch (NoSuchElementException ex) {
            logger.error("User wasn't found in DB: guid = {}", guid);
            throw new UserServiceException("User wasn't found");
        }
        return user;
    }
}
