package com.softserve.itacademy.kek.modelInterfaces;


import java.util.UUID;

/**
 * Interface for exchange data with user service layer
 */
public interface IUser {

    UUID getGuid();

    String getEmail();

    String getPhoneNumber();

    String getName();

    String getNickname();

    IDetails getUserDetails();

}
