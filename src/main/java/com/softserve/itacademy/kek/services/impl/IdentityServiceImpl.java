package com.softserve.itacademy.kek.services.impl;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.exception.IdentityServiceException;
import com.softserve.itacademy.kek.models.IIdentity;
import com.softserve.itacademy.kek.models.enums.IdentityTypeEnum;
import com.softserve.itacademy.kek.models.impl.Identity;
import com.softserve.itacademy.kek.models.impl.IdentityType;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.IdentityRepository;
import com.softserve.itacademy.kek.repositories.IdentityTypeRepository;
import com.softserve.itacademy.kek.services.IIdentityService;
import com.softserve.itacademy.kek.services.IUserService;

@Service
public class IdentityServiceImpl implements IIdentityService {

    private final static Logger logger = LoggerFactory.getLogger(IdentityServiceImpl.class);
    private IdentityRepository identityRepository;
    private IdentityTypeRepository identityTypeRepository;
    private IUserService userService;

    @Autowired
    public IdentityServiceImpl(IdentityRepository identityRepository,
                               IdentityTypeRepository identityTypeRepository,
                               IUserService userService) {
        this.identityRepository = identityRepository;
        this.identityTypeRepository = identityTypeRepository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public IIdentity create(UUID userGuid, IdentityTypeEnum type, String payload) throws IdentityServiceException {
        logger.info("Insert identity into DB: userGuid = {}, type = {}", userGuid, type);

        final User user = (User) userService.getByGuid(userGuid);
        final IdentityType identityType = internalCreateIdentityType(type);
        final Identity identity = new Identity(user, identityType, payload);

        try {
            final Identity insertedIdentity = identityRepository.saveAndFlush(identity);

            logger.debug("Identity was inserted into DB: {}", insertedIdentity);

            return insertedIdentity;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while inserting identity into DB: " + identity, ex);
            throw new IdentityServiceException("An error occurred while inserting identity", ex);
        }
    }

    @Override
    @Transactional
    public IIdentity read(UUID userGuid, IdentityTypeEnum type) throws IdentityServiceException {
        logger.info("Read identity from DB: userGuid = {}, type = {}", userGuid, type.name());
        return internalGetIdentity(userGuid, type);
    }

    @Override
    @Transactional
    public IIdentity update(UUID userGuid, IdentityTypeEnum type, String payload) throws IdentityServiceException {
        logger.info("Update identity in DB: userGuid = {}, type = {}", userGuid, type);

        final Identity identity = internalGetIdentity(userGuid, type);
        identity.setPayload(payload);

        try {
            final Identity updatedIdentity = identityRepository.saveAndFlush(identity);

            logger.debug("Identity was updated in DB: {}", updatedIdentity);

            return updatedIdentity;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while updating identity in DB: " + identity, ex);
            throw new IdentityServiceException("An error occurred while updating identity", ex);
        }
    }

    @Transactional
    @Override
    public void delete(UUID userGuid, IdentityTypeEnum type) throws IdentityServiceException {
        logger.info("Delete identity from DB: userGuid = {}, type = {}", userGuid, type);

        final Identity identity = internalGetIdentity(userGuid, type);

        try {
            identityRepository.deleteById(identity.getIdIdentity());
            identityRepository.flush();

            logger.debug("Identity was deleted from DB: identity = {}", identity);
        } catch (DataAccessException ex) {
            logger.error("Error while deleting identity from DB: " + identity, ex);
            throw new IdentityServiceException("An error occurred while deleting identity", ex);
        }
    }

    private Identity internalGetIdentity(UUID userGuid, IdentityTypeEnum type) {
        logger.info("Get identity from DB: userGuid = {}, type = {}", userGuid, type);

        final String typeName = type.name();

        try {
            return identityRepository.findByUserGuidAndIdentityTypeName(userGuid, typeName).orElseThrow();
        } catch (NoSuchElementException | DataAccessException ex) {
            logger.error("No such identity in DB", ex);
            throw new IdentityServiceException("There is no such identity", ex);
        }
    }

    private IdentityType internalCreateIdentityType(IdentityTypeEnum type) {
        logger.info("Get identity type: {}", type.name());

        try {
            IdentityType identityType = identityTypeRepository.findByName(type.name()).orElseThrow(() -> {
                logger.error("Identity type wasn't found in the database");
                return new IdentityServiceException("Identity type was not found in database for name: " + type.name(), new NoSuchElementException());
            });

            if (identityType == null) {
                logger.info("Insert identity type: {}", type);

                final IdentityType newIdentityType = new IdentityType(type);
                identityType = identityTypeRepository.saveAndFlush(newIdentityType);

                logger.debug("Identity type was inserted into DB: {}", identityType);
            }

            return identityType;

        } catch (DataAccessException ex) {
            logger.error("Error while getting identity type " + type.name(), ex);
            throw new IdentityServiceException("An error occurred while getting identity type", ex);
        }
    }
}
