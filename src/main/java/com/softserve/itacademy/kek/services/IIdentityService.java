package com.softserve.itacademy.kek.services;


import java.util.UUID;

import com.softserve.itacademy.kek.exception.IdentityServiceException;
import com.softserve.itacademy.kek.models.IIdentity;
import com.softserve.itacademy.kek.models.enums.IdentityTypeEnum;

public interface IIdentityService {

    /**
     * Inserts new user identity into db
     *
     * @param userGuid user GUID
     * @param type     identity type
     * @param payload  payload
     * @return inserted user identity
     * @throws IdentityServiceException
     */
    IIdentity create(UUID userGuid, IdentityTypeEnum type, String payload) throws IdentityServiceException;

    /**
     * Get user identity from db
     *
     * @param userGuid user GUID
     * @param type     predefined identity type
     * @return user password
     * @throws IdentityServiceException
     */
    IIdentity read(UUID userGuid, IdentityTypeEnum type) throws IdentityServiceException;

    /**
     * Updates user identity
     *
     * @param userGuid user data
     * @param type     identity type
     * @param payload  payload
     * @return Identity
     * @throws IdentityServiceException
     */
    IIdentity update(UUID userGuid, IdentityTypeEnum type, String payload) throws IdentityServiceException;

    /**
     * Delete user indentity
     *
     * @param userGuid user GUID
     * @param type     identity type
     * @throws IdentityServiceException
     */
    void delete(UUID userGuid, IdentityTypeEnum type) throws IdentityServiceException;
}
