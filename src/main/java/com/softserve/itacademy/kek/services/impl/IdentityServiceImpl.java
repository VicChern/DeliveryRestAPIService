package com.softserve.itacademy.kek.services.impl;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.exception.IdentityServiceException;
import com.softserve.itacademy.kek.models.IIdentity;
import com.softserve.itacademy.kek.models.enums.IdentityTypeDef;
import com.softserve.itacademy.kek.models.impl.Identity;
import com.softserve.itacademy.kek.models.impl.IdentityType;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.IdentityRepository;
import com.softserve.itacademy.kek.repositories.IdentityTypeRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IIdentityService;

@Service
public class IdentityServiceImpl implements IIdentityService {

    private final static Logger logger = LoggerFactory.getLogger(IdentityServiceImpl.class);
    private IdentityRepository identityRepository;
    private IdentityTypeRepository identityTypeRepository;
    private UserRepository userRepository;

    @Autowired
    public IdentityServiceImpl(IdentityRepository identityRepository,
                               IdentityTypeRepository identityTypeRepository,
                               UserRepository userRepository) {
        this.identityRepository = identityRepository;
        this.identityTypeRepository = identityTypeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public IIdentity create(UUID userGuid, IdentityTypeDef type, String payload) throws IdentityServiceException {
        logger.info("Creating identity in DB: user.guid = {}, type = {}", userGuid, type);

        final User user = internalGetUserByGuid(userGuid);
        final IdentityType identityType = internalGetForcedIdentityType(type);
        final Identity identity = new Identity(user, identityType, payload);

        try {
            final Identity dbIdentity = identityRepository.saveAndFlush(identity);

            logger.debug("Identity was inserted into DB: {}", identity);

            return dbIdentity;
        } catch (DataAccessException ex) {
            logger.error("Identity was not inserted into DB: " + identity, ex);
            throw new IdentityServiceException("An error occurred while creating identity ", ex);
        }
    }

    @Override
    @Transactional
    public IIdentity read(UUID userGuid, IdentityTypeDef type) {
        logger.info("Reading identity: user.guid = {}, type = {}", userGuid, type.name());
        return internalGetIdentity(userGuid, type);
    }

    @Override
    @Transactional
    public IIdentity update(UUID userGuid, IdentityTypeDef type, String payload) throws IdentityServiceException {
        logger.info("Updating identity in DB: user.guid = {}, type = {}", userGuid, type);

        final Identity identity = internalGetIdentity(userGuid, type);

        logger.info("Setting new identity: user.guid = {}, type = {}", userGuid, type);

        identity.setPayload(payload);

        try {
            final Identity dbIdentity = identityRepository.saveAndFlush(identity);

            logger.info("Identity was updated in DB: {}", dbIdentity);

            return dbIdentity;
        } catch (DataAccessException ex) {
            logger.info("Identity was not updated: {} " + identity, ex);
            throw new IdentityServiceException("An error occurred while updating identity ", ex);
        }
    }

    @Transactional
    @Override
    public void delete(UUID userGuid, IdentityTypeDef type) {
        logger.info("Deleting identity from the DB: user.guid = {}, type = {}", userGuid, type);

        final Identity identity = internalGetIdentity(userGuid, type);

        try {
            identityRepository.deleteById(identity.getIdIdentity());
            identityRepository.flush();
        } catch (DataAccessException ex) {
            logger.error("Identity was not deleted from DB: " + identity, ex);
        }
    }

    private Identity internalGetIdentity(UUID userGuid, IdentityTypeDef type) throws IdentityServiceException {
        logger.debug("Find Identity in DB: user.guid = {}, type = {}", userGuid, type);

        try {
            return identityRepository.findByUserGuidAndIdentityTypeName(userGuid, type.name()).orElseThrow();
        } catch (NoSuchElementException | DataAccessException ex) {
            logger.error("No such identity in DB", ex);
            throw new IdentityServiceException("There is no such identity", ex);
        }
    }

    private IdentityType internalGetForcedIdentityType(IdentityTypeDef type) {
        logger.debug("Getting Identity Type: {}", type);

        try {
            IdentityType dbIdentityType = identityTypeRepository.findByName(type.name());

            if (dbIdentityType == null) {
                logger.debug("Inserting Identity Type: {}", type);

                final IdentityType newType = new IdentityType(type);

                dbIdentityType = identityTypeRepository.saveAndFlush(newType);

                logger.debug("Identity Type was inserted into DB: {}", dbIdentityType);
            }

            return dbIdentityType;
        } catch (DataAccessException ex) {
            logger.error("An error occurred while getting Identity Type " + type, ex);
            throw new IdentityServiceException("An error occurred while getting identity type", ex);
        }
    }

    private User internalGetUserByGuid(UUID guid) throws IdentityServiceException {
        logger.debug("Find User in DB: guid = {}", guid);

        try {
            return userRepository.findByGuid(guid).orElseThrow();
        } catch (NoSuchElementException | DataAccessException ex) {
            logger.error("User was not found in DB: guid = {}", guid);
            throw new IdentityServiceException("The user was not found in the Database", ex);
        }
    }
}
