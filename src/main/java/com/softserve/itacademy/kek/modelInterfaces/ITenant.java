package com.softserve.itacademy.kek.modelInterfaces;

import java.util.UUID;

/**
 * Interface for exchange data with tenant service layer
 */
public interface ITenant {

    UUID getGuid();

    String getName();

    IUser getTenantOwner();

    IDetails getTenantDetails();

}
