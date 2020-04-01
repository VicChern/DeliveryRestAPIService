package com.softserve.itacademy.kek.services;


import java.util.UUID;

import com.softserve.itacademy.kek.exception.IdentityServiceException;
import com.softserve.itacademy.kek.models.IIdentity;
import com.softserve.itacademy.kek.models.enums.IdentityTypeDef;

public interface IIdentityService {

    /**
     * Inserts new user identity into db
     *
     * @param userGuid user GUID
     * @param type     identity type
     * @param payload  payload
     * @return inserted user identity
     */
    IIdentity create(UUID userGuid, IdentityTypeDef type, String payload);

    /**
     * Get user identity from db
     *
     * @param userGuid user GUID
     * @param type     predefined identity type
     * @return user password
     */
    IIdentity read(UUID userGuid, IdentityTypeDef type);

    /**
     * Updates user identity
     *
     * @param userGuid user data
     * @param type     identity type
     * @param payload  payload
     * @return Identity
     * @throws IdentityServiceException
     */
    IIdentity update(UUID userGuid, IdentityTypeDef type, String payload) throws IdentityServiceException;

    /**
     * Delete user indentity
     *
     * @param userGuid user GUID
     * @param type     identity type
     */
    void delete(UUID userGuid, IdentityTypeDef type) throws IdentityServiceException;
}
