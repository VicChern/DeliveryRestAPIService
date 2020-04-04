package com.softserve.itacademy.kek.models;

/**
 * Interface for User details data exchange with service and public api layers
 */
public interface IUserDetails {
    /**
     * Returns user payload
     *
     * @return user payload
     */
    String getPayload();

    /**
     * Returns user image url
     *
     * @return user image url
     */
    String getImageUrl();
}
