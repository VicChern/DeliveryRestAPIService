package com.softserve.itacademy.kek.cloudstorage;

import java.util.List;

public interface ICloudStorageService {
    CloudStorageObject uploadBinaryData(byte[] data) throws CloudStorageServiceException;
    CloudStorageObject getCloudStorageObject(String guid) throws CloudStorageServiceException;
    List<CloudStorageObject> getCloudStorageObjects(String filter) throws CloudStorageServiceException;
}
