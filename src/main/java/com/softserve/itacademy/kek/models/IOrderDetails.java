package com.softserve.itacademy.kek.models;

/**
 * Interface for OrderDetails data exchange with service and public api layers
 */
public interface IOrderDetails {

    /**
     * Returns Order's payload
     *
     * @return Order's payload
     */
    String getPayload();

    /**
     * Returns Order's imageUrl
     *
     * @return Order's imageUrl
     */
    String getImageUrl();
}
