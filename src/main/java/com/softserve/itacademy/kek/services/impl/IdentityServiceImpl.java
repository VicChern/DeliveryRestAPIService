package com.softserve.itacademy.kek.services.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.exception.IdentityServiceException;
import com.softserve.itacademy.kek.exception.NoSuchIdentityException;
import com.softserve.itacademy.kek.models.IIdentity;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.Identity;
import com.softserve.itacademy.kek.models.impl.IdentityType;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.IdentityRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IIdentityService;

@Service
public class IdentityServiceImpl implements IIdentityService {

    private final static Logger logger = LoggerFactory.getLogger(IdentityServiceImpl.class);
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public IdentityRepository identityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public IdentityServiceImpl(IdentityRepository identityRepository) {
        this.identityRepository = identityRepository;
    }

    @Transactional
    @Override
    public IIdentity create(IUser user, String password) {
        logger.info("Saving password for user: {}", user);
        Identity identity = new Identity((User) user, password);
        IdentityType type = new IdentityType();
        type.setName("key");
        identity.setIdentityType(type);

        try {
            Identity dbIdentity = identityRepository.saveAndFlush(identity);
            logger.debug("Identity was inserted into DB: {}", identity);

            return dbIdentity;
        } catch (DataAccessException ex) {
            logger.error("Identity was not inserted into DB: " + identity, ex);
            throw new IdentityServiceException("An error occurred while creatind identity ", ex);
        }
    }


    @Override
    @Transactional
    public IIdentity read(String email) {
        logger.info("Getting password for user with email: {}", email);

        try {
            logger.info("Found identity for user with email: {}", email);
            return identityRepository.findByUserEmailAndIdentityTypeName(email, "key");
        } catch (DataAccessException ex) {
            logger.info("No such identity for user: email: {}" + email, ex);
            throw new NoSuchIdentityException("There is no such identity ", ex);
        }
    }

    @Override
    @Transactional
    public IIdentity update(IUser user, String password) {
        logger.info("Getting request for new password for user: {}", user);

        Identity identity = null;
        try {
            logger.info("Found identity for user: {}", user.getGuid());
            identity = identityRepository.findByUserEmailAndIdentityTypeName(user.getEmail(), "key");
        } catch (DataAccessException ex) {
            logger.info("No such identity for user: email: {}" + user.getEmail(), ex);
            throw new NoSuchIdentityException("There is no such identity ", ex);
        }

        logger.info("Setting new identity for user: {}", user.getGuid());
        identity.setPayload(passwordEncoder.encode(password));

        try {
            Identity dbIdentity = identityRepository.saveAndFlush(identity);
            logger.info("Identity was updated in DB: {}", dbIdentity);

            return dbIdentity;
        } catch (DataAccessException ex) {
            logger.info("Identity was not updated: {} " + identity, ex);
            throw new IdentityServiceException("An error occurred while updating identity ", ex);
        }
    }

    @Transactional
    @Override
    public void delete(IIdentity identity) {
        logger.info("Deleting identity from the DB: {}", identity);

        try {
            identityRepository.deleteById(identity.getIdIdentity());
            identityRepository.flush();
        } catch (DataAccessException ex) {
            logger.error("Identity was not deleted from DB: " + identity, ex);
        }
    }
}
