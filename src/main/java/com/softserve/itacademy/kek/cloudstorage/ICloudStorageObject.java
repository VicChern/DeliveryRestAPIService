package com.softserve.itacademy.kek.cloudstorage;

public interface ICloudStorageObject {
    String getUrlString();
    String getGuid();
    byte[] getDataBytes();
}
