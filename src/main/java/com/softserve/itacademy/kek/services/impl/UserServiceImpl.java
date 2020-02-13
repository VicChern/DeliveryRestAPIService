package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.exception.UserServiceException;
import com.softserve.itacademy.kek.modelInterfaces.IUser;
import com.softserve.itacademy.kek.modelInterfaces.IUserDetails;
import com.softserve.itacademy.kek.models.User;
import com.softserve.itacademy.kek.models.UserDetails;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.ITenantService;
import com.softserve.itacademy.kek.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    final Logger logger = LoggerFactory.getLogger(ITenantService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Inserts new user into DB
     *
     * @param user user data
     * @return inserted user data
     */
    @Transactional
    @Override
    public IUser create(IUser user) {
        logger.info("Insert User into DB: {}", user);

        User realUser = new User();

        realUser.setGuid(UUID.randomUUID());
        realUser.setName(user.getName());
        realUser.setNickname(user.getNickname());
        realUser.setEmail(user.getEmail());
        realUser.setPhoneNumber(user.getPhoneNumber());

        UserDetails realDetails = new UserDetails();

        IUserDetails details = user.getUserDetails();
        if (details != null) {
            realDetails.setImageUrl(details.getImageUrl());
            realDetails.setPayload(details.getPayload());
        }

        realUser.setUserDetails(realDetails);

        try {
            realUser = userRepository.save(realUser);
        } catch (PersistenceException | ConstraintViolationException ex) {
            logger.error("User wasn't inserted into DB: {}", realUser, ex);
            throw new UserServiceException("User wasn't saved");
        }

        logger.info("User was inserted into DB: guid = {}", realUser.getGuid());

        return realUser;
    }

    /**
     * Updates user data
     *
     * @param user user data
     * @return updated user data
     */
    @Transactional
    @Override
    public IUser update(IUser user) {
        logger.info("Update User in DB: {}", user);

        //User realUser = findRealUser(user.getGuid());
        User realUser = findRealUser(UUID.fromString("5a15adfc-84e9-451a-a3db-4d86879dc0a9"));
        realUser.setName(user.getName());
        realUser.setNickname(user.getNickname());
        realUser.setEmail(user.getEmail());
        realUser.setPhoneNumber(user.getPhoneNumber());

        UserDetails realDetails = new UserDetails();
        realDetails.setIdUser(realUser.getIdUser());

        IUserDetails details = user.getUserDetails();
        if (details != null) {
            realDetails.setImageUrl(details.getImageUrl());
            realDetails.setPayload(details.getPayload());
        }

        realUser.setUserDetails(realDetails);

        try {
            realUser = userRepository.save(realUser);
        } catch (PersistenceException | ConstraintViolationException ex) {
            logger.error("User wasn't updated in DB: {}", realUser);
            throw new UserServiceException("User wasn't saved");
        }

        logger.info("User was updated in DB: guid = {}", realUser.getGuid());

        return realUser;
    }

    /**
     * Deletes user from DB by user guid
     *
     * @param guid user guid
     */
    @Transactional
    @Override
    public void delete(UUID guid) {
        logger.info("Delete User from DB: guid = {}", guid);

        User realUser = findRealUser(guid);

        try {
            userRepository.deleteById(realUser.getIdUser());
        } catch (PersistenceException ex) {
            logger.error("User wasn't deleted from DB: {}", realUser);
            throw new UserServiceException("User wasn't deleted");
        }

        logger.info("User was deleted from DB: guid = {}", realUser.getGuid());
    }

    /**
     * Returns user data by user guid
     *
     * @param guid user guid
     * @return user data
     */
    @Override
    public IUser getByGuid(UUID guid) {
        return findRealUser(guid);
    }

    @Transactional(readOnly = true)
    private User findRealUser(UUID guid) {
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
