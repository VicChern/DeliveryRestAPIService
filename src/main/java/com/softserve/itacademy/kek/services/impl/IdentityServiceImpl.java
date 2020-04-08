package com.softserve.itacademy.kek.services.impl;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final IdentityRepository identityRepository;
    private final IdentityTypeRepository identityTypeRepository;
    private final IUserService userService;

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

        try {
            final User user = (User) userService.getByGuid(userGuid);

            final IdentityType identityType = identityTypeRepository.findByName(type.toString())
                    .orElseThrow(() -> new NoSuchElementException("Identity type was not found"));

            final Identity identity = new Identity(user, identityType, payload);

            final Identity insertedIdentity = identityRepository.saveAndFlush(identity);

            logger.debug("Identity was inserted into DB: userGuid = {}, type = {}", userGuid, type);

            return insertedIdentity;
        } catch (Exception ex) {
            logger.error("Error while inserting identity into DB", ex);
            throw new IdentityServiceException("An error occurred while inserting identity", ex);
        }
    }

    @Transactional
    @Override
    public IIdentity update(UUID userGuid, IdentityTypeEnum type, String payload) throws IdentityServiceException {
        logger.info("Update identity in DB: userGuid = {}, type = {}", userGuid, type);

        final Identity identity = (Identity) get(userGuid, type);

        try {
            identity.setPayload(payload);

            final Identity updatedIdentity = identityRepository.saveAndFlush(identity);

            logger.debug("Identity was updated in DB: userGuid = {}, identityType = {}", userGuid, type);

            return updatedIdentity;
        } catch (Exception ex) {
            logger.error("Error while updating identity in DB", ex);
            throw new IdentityServiceException("An error occurred while updating identity", ex);
        }
    }

    @Transactional
    @Override
    public void delete(UUID userGuid, IdentityTypeEnum type) throws IdentityServiceException {
        logger.info("Delete identity from DB: userGuid = {}, type = {}", userGuid, type);

        final Identity identity = (Identity) get(userGuid, type);

        try {
            identityRepository.deleteById(identity.getIdIdentity());
            identityRepository.flush();

            logger.debug("Identity was deleted from DB: userGuid = {}, type = {}", userGuid, type);
        } catch (Exception ex) {
            logger.error("Error while deleting identity from DB", ex);
            throw new IdentityServiceException("An error occurred while deleting identity", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IIdentity get(UUID userGuid, IdentityTypeEnum type) throws IdentityServiceException {
        logger.info("Get identity from DB: userGuid = {}, type = {}", userGuid, type);

        try {
            final Identity identity = identityRepository.findByUserGuidAndIdentityTypeName(userGuid, type.toString())
                    .orElseThrow(() -> new NoSuchElementException("Identity was not found"));

            logger.debug("Identity was found in DB: {}", identity);

            return identity;
        } catch (Exception ex) {
            logger.error("Error while getting identity from DB", ex);
            throw new IdentityServiceException("An error occurred while getting identity", ex);
        }
    }
}
