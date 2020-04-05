package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.models.impl.IdentityType;

/**
 * Interface for Identity data exchange with service and public api layers
 */
public interface IIdentity {
    /**
     * Returns idIdentity
     *
     * @return idIdentity
     */
    Long getIdIdentity();

    /**
     * Returns identityType
     *
     * @return identityType
     */
    IdentityType getIdentityType();

    /**
     * Returns user
     *
     * @return user
     */
    IUser getUser();

    /**
     * Returns payload
     *
     * @return payload
     */
    String getPayload();
}
