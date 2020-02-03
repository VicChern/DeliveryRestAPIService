package com.softserve.itacademy.kek.cloudstorage;

import com.softserve.itacademy.kek.exception.CloudStorageServiceException;

import java.util.List;

public interface ICloudStorageService {
    CloudStorageObject uploadBinaryData(byte[] data) throws CloudStorageServiceException;

    CloudStorageObject getCloudStorageObject(String guid) throws CloudStorageServiceException;

    List<CloudStorageObject> getCloudStorageObjects(String filter) throws CloudStorageServiceException;
}
