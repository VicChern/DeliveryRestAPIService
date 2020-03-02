package com.softserve.itacademy.kek.models;

import java.util.UUID;

/**
 * Interface for Order data exchange with service layer
 */
public interface IOrder {

    /**
     * Returns tenant
     *
     * @return tenant
     */
    ITenant getTenant();

    /**
     * Returns Order's GUID
     *
     * @return Order's GUID
     */
    UUID getGuid();

    /**
     * Returns Order's summary
     *
     * @return Order's summary
     */
    String getSummary();

    /**
     * Returns Order's details
     *
     * @return Order's details
     */
    IOrderDetails getOrderDetails();
}
