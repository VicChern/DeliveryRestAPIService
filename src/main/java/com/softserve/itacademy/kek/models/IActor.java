package com.softserve.itacademy.kek.models;

import java.util.UUID;

/**
 * Interface for Actor data exchange with service and public api layers
 */
public interface IActor {

    /**
     * Returns tenant
     *
     * @return tenant
     */
    ITenant getTenant();

    /**
     * Returns user
     *
     * @return user
     */
    IUser getUser();

    /**
     * Returns Actor's GUID
     *
     * @return Actor's GUID
     */
    UUID getGuid();

    /**
     * Returns Actor's alias
     *
     * @return Actor's alias
     */
    String getAlias();

}
