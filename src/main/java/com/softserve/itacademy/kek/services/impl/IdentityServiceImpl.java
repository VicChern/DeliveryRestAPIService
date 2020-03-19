package com.softserve.itacademy.kek.services.impl;

import javax.transaction.Transactional;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.models.IIdentity;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.Identity;
import com.softserve.itacademy.kek.models.impl.IdentityType;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.IdentityRepository;
import com.softserve.itacademy.kek.repositories.IdentityTypeRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IIdentityService;

@Service
public class IdentityServiceImpl implements IIdentityService {

    private final IdentityRepository identityRepository;
    private final static Logger logger = LoggerFactory.getLogger(IdentityServiceImpl.class);

    @Autowired
    public IdentityServiceImpl(IdentityRepository identityRepository) {
        this.identityRepository = identityRepository;
    }

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public IdentityTypeRepository identityTypeRepository;

    @Transactional
    @Override
    public IIdentity savePassword(IUser user, String password) {
        logger.info("Saving password for user: {}", user);
        User user1 = (User) user;
        Identity identity = new Identity(user1, password);
        IdentityType type = new IdentityType();
        type.setName("key");
        identity.setIdentityType(type);

        try {
            identityRepository.saveAndFlush(identity);
            logger.debug("Identity was inserted into DB: {}", identity);
        } catch (DataAccessException ex) {
            logger.error("Identity was not inserted into DB: " + identity, ex);
        }

        return identity;
    }


    @Override
    public IIdentity getPassword(String email) {
        User user = userRepository.findByEmail(email);

        IdentityType type = identityTypeRepository.findByName("key");


        Identity identity = null;
//
//        for (Identity i : user.getIdentityList()) {
//            if (i.getIdentityType().equals("key")) {
//                return i;
//            }
//        }
//
        return identity;
    }

    @Override
    @Transactional
    public IIdentity updatePassword(User user, String password) {
        logger.info("Saving new password for user: {}", user);
//        Identity identity = identityRepository.findById(user.getIdentityList())
//
//        try {
//
//            logger.debug("Identity was updated in DB: {}", identity);
//        } catch (DataAccessException ex) {
//            logger.error("Identity was not inserted into DB: " + identity, ex);
//        }

        return null;
    }

    @Transactional
    @Override
    public void delete(Identity identity) {
        logger.info("Deleting identity from the DB: {}", identity);

        try {
            identityRepository.deleteById(identity.getIdIdentity());
            identityRepository.flush();
        } catch (DataAccessException ex) {
            logger.error("Identity was not deleted from DB: " + identity, ex);
        }
    }
}
