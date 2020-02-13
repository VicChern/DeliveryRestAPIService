package com.softserve.itacademy.kek.modelInterfaces;

/**
 * Interface for User details data exchange with Service Layer
 */
public interface IUserDetailsData {
    /**
     * Returns user payload
     * @return user payload
     */
    String getPayload();

    /**
     * Returns user image url
     * @return user image url
     */
    String getImageUrl();
}
