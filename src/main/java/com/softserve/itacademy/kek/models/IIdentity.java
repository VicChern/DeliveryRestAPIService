package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.models.impl.IdentityType;

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
