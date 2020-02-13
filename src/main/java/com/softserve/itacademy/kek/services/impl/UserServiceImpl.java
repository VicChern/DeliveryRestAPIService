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

import javax.persistence.PersistenceException;
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
     * Inserts new user to db
     *
     * @param userData user data
     * @return inserted user data
     */
    public IUser insert(IUser userData) {
        logger.info("Insert User into DB: " + userData);

        User user = new User();
        user.setGuid(UUID.randomUUID());
        user.setName(userData.getName());
        user.setNickname(userData.getNickname());
        user.setEmail(userData.getEmail());
        user.setPhoneNumber(userData.getPhoneNumber());

        UserDetails details = new UserDetails();

        IUserDetails detailsData = userData.getUserDetailsData();
        if (detailsData != null) {
            details.setImageUrl(detailsData.getImageUrl());
            details.setPayload(detailsData.getPayload());
        }

        user.setUserDetails(details);

        try {
            user = userRepository.save(user);
        } catch (PersistenceException ex) {
            logger.error("User wasn't inserted into DB: " + user);
            throw new UserServiceException("User wasn't saved");
        }

        logger.info("User was inserted into DB, guid: " + user.getGuid());

        return user;
    }

    /**
     * Updates user
     *
     * @param userData user data
     * @return updated user data
     */
    @Override
    public IUser update(IUser userData) {
        logger.info("Update User in DB: " + userData);

        User user = getEntityByGuid(userData.getGuid());
        user.setName(userData.getName());
        user.setNickname(userData.getNickname());
        user.setEmail(userData.getEmail());
        user.setPhoneNumber(userData.getPhoneNumber());

        IUserDetails detailsData = userData.getUserDetailsData();
        if (detailsData != null) {
            UserDetails details = user.getUserDetails();
            details.setImageUrl(detailsData.getImageUrl());
            details.setPayload(detailsData.getPayload());
        }

        try {
            user = userRepository.save(user);
        } catch (PersistenceException ex) {
            logger.error("User wasn't updated in DB: " + user);
            throw new UserServiceException("User wasn't saved");
        }

        logger.info("User was updated in DB, guid: " + user.getGuid());

        return user;
    }

    /**
     * Deletes user from DB by user guid
     *
     * @param guid user guid
     */
    @Override
    public void deleteByGuid(UUID guid) {
        logger.info("Delete User from DB, guid: " + guid);

        User user = getEntityByGuid(guid);

        try {
            userRepository.deleteById(user.getIdUser());
        } catch (PersistenceException ex) {
            logger.error("User wasn't deleted from DB: " + user);
            throw new UserServiceException("User wasn't deleted");
        }

        logger.info("User was deleted from DB, guid: " + user.getGuid());
    }

    /**
     * Returns user data by user guid
     *
     * @param guid user guid
     * @return user data
     */
    @Override
    public IUser getByGuid(UUID guid) {
        return getEntityByGuid(guid);
    }

    private User getEntityByGuid(UUID guid) {
        logger.info("Find User in DB, guid: " + guid);

        User user;
        try {
            user = userRepository.findByGuid(guid);
        } catch (NoSuchElementException ex) {
            logger.error("User wasn't found in DB, guid: " + guid);
            throw new UserServiceException("User wasn't found");
        }
        return user;
    }
}
