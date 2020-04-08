package com.softserve.itacademy.kek.models.services;

/**
 * Interface for CloudStorageObject data exchange with Service Layer
 */
public interface ICloudStorageObject {

    /**
     * Returns URL of the specific Google Cloud Storage object
     *
     * @return URL
     */
    String getUrlString();

    /**
     * Returns GUID of the specific Google Cloud Storage object
     *
     * @return unique GUID
     */
    String getGuid();

    /**
     * Returns data as byte array of the specific Google Cloud storage object
     *
     * @return byte array
     */
    byte[] getDataBytes();
}
