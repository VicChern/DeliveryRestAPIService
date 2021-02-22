package com.softserve.itacademy.kek.services;


import java.util.UUID;

import com.softserve.itacademy.kek.exception.IdentityServiceException;
import com.softserve.itacademy.kek.models.IIdentity;
import com.softserve.itacademy.kek.models.enums.IdentityTypeEnum;

/**
 * Service interface for {@link IIdentity}
 */
public interface IIdentityService {

    /**
     * Inserts new user identity into db
     *
     * @param userGuid user GUID
     * @param type     identity type
     * @param payload  payload
     * @return inserted user identity
     * @throws IdentityServiceException if an error occurred
     */
    IIdentity create(UUID userGuid, IdentityTypeEnum type, String payload) throws IdentityServiceException;

    /**
     * Gets user identity from db
     *
     * @param userGuid user GUID
     * @param type     predefined identity type
     * @return user password
     * @throws IdentityServiceException if an error occurred
     */
    IIdentity get(UUID userGuid, IdentityTypeEnum type) throws IdentityServiceException;

    /**
     * Updates user identity
     *
     * @param userGuid user data
     * @param type     identity type
     * @param payload  payload
     * @return Identity
     * @throws IdentityServiceException if an error occurred
     */
    IIdentity update(UUID userGuid, IdentityTypeEnum type, String payload) throws IdentityServiceException;

    /**
     * Deletes user indentity
     *
     * @param userGuid user GUID
     * @param type     identity type
     * @throws IdentityServiceException if an error occurred
     */
    void delete(UUID userGuid, IdentityTypeEnum type) throws IdentityServiceException;
}
