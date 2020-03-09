package com.softserve.itacademy.kek.services.impl;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.UserServiceException;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.IUserDetails;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.models.impl.UserDetails;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public IUser create(IUser userData) {
        logger.info("Insert User into DB: {}", userData);

        User user = new User();

        user.setGuid(UUID.randomUUID());
        user.setName(userData.getName());
        user.setNickname(userData.getNickname());
        user.setEmail(userData.getEmail());
        user.setPhoneNumber(userData.getPhoneNumber());

        UserDetails details = new UserDetails();
        user.setUserDetails(details);

        IUserDetails detailsData = userData.getUserDetails();
        if (detailsData != null) {
            details.setImageUrl(detailsData.getImageUrl());
            details.setPayload(detailsData.getPayload());
        }

        try {
            user = userRepository.save(user);
        } catch (PersistenceException | ConstraintViolationException ex) {
            logger.error("User wasn't inserted into DB: " + user, ex);
            throw new UserServiceException("User wasn't inserted");
        }

        logger.info("User was inserted into DB: {}", user);

        return user;
    }

    @Transactional
    @Override
    public IUser update(IUser userData) {
        logger.info("Update User in DB: {}", userData);

        User user = findUserByGuid(userData.getGuid());
        user.setName(userData.getName());
        user.setNickname(userData.getNickname());
        user.setEmail(userData.getEmail());
        user.setPhoneNumber(userData.getPhoneNumber());

        IUserDetails detailsData = userData.getUserDetails();
        if (detailsData != null) {
            UserDetails details = new UserDetails();
            user.setUserDetails(details);

            details.setIdUser(user.getIdUser());
            details.setImageUrl(detailsData.getImageUrl());
            details.setPayload(detailsData.getPayload());
        }

        try {
            user = userRepository.save(user);
        } catch (PersistenceException | ConstraintViolationException ex) {
            logger.error("User wasn't updated in DB: " + user, ex);
            throw new UserServiceException("User wasn't updated");
        }

        logger.info("User was updated in DB: {}", user);

        return user;
    }

    @Transactional
    @Override
    public void deleteByGuid(UUID guid) {
        logger.info("Delete User from DB: guid = {}", guid);

        User user = findUserByGuid(guid);

        try {
            userRepository.deleteById(user.getIdUser());
        } catch (PersistenceException ex) {
            logger.error("User wasn't deleted from DB: " + user, ex);
            throw new UserServiceException("User wasn't deleted");
        }

        logger.info("User was deleted from DB: {}", user);
    }

    @Transactional(readOnly = true)
    @Override
    public IUser getByGuid(UUID guid) {
        logger.info("Get User from DB: guid = {}", guid);
        return findUserByGuid(guid);
    }

    @Transactional(readOnly = true)
    @Override
    public List<IUser> getAll() {
        logger.info("Get all Users");

        List<? extends IUser> users = userRepository.findAll();

        return (List<IUser>) users;
    }

    private User findUserByGuid(UUID guid) {
        logger.info("Find User in DB: guid = {}", guid);

        User user = userRepository.findByGuid(guid);
        if (user == null) {
            logger.warn("User wasn't found in DB: guid = {}", guid);
            throw new UserServiceException("User wasn't found");
        }
        return user;
    }
}
