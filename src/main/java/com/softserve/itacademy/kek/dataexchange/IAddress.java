package com.softserve.itacademy.kek.dataexchange;

import java.util.UUID;

/**
 * Interface for Address data exchange with service layer
 */
public interface IAddress {

    /**
     * Returns address GUID
     *
     * @return address GUID
     */
    UUID getGuid();

    /**
     * Returns address
     *
     * @return address
     */
    String getAddress();

    /**
     * Returns address notes
     *
     * @return address notes
     */
    String getNotes();

    /**
     * Returns address alias
     *
     * @return address alias
     */
    String getAlias();

}
