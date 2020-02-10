package com.softserve.itacademy.kek.model;

public interface ICloudStorageObject {

    String getUrlString();

    String getGuid();

    byte[] getDataBytes();
}
