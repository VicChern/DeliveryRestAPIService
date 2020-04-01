package com.softserve.itacademy.kek.services;

import java.util.List;

import com.softserve.itacademy.kek.exception.CloudStorageServiceException;
import com.softserve.itacademy.kek.models.ICloudStorageObject;

public interface ICloudStorageService {
    /**
     * Uploads byte array to Google Cloud Storage
     *
     * @param data array for uploading
     * @return CloudStorageObject with unique GUID
     * @throws CloudStorageServiceException
     */
    ICloudStorageObject uploadBinaryData(byte[] data) throws CloudStorageServiceException;

    /**
     * Gets stored data from Google Cloud Storage bucket by GUID
     *
     * @param guid unique name of object in Google Cloud Storage bucket
     * @return CloudStorageObject with unique GUID
     * @throws CloudStorageServiceException
     */
    ICloudStorageObject getCloudStorageObject(String guid) throws CloudStorageServiceException;

    /**
     * Gets list of stored objects from Google Cloud Storage bucket
     *
     * @param filter bucket name
     * @return list of CloudStorageObjects from bucket
     * @throws CloudStorageServiceException
     */
    List<ICloudStorageObject> getCloudStorageObjects(String filter) throws CloudStorageServiceException;
}
