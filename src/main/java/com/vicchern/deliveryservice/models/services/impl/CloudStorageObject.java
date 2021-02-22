package com.vicchern.deliveryservice.models.services.impl;

import java.util.Arrays;
import java.util.Objects;

import com.vicchern.deliveryservice.models.services.ICloudStorageObject;

public class CloudStorageObject implements ICloudStorageObject {
    private final String url;
    private final String guid;
    private final byte[] data;

    public CloudStorageObject() {
        this(null, null, null);
    }

    public CloudStorageObject(String url, String guid, byte[] data) {
        this.url = url;
        this.guid = guid;
        this.data = data;
    }

    @Override
    public String getUrlString() {
        return url;
    }

    @Override
    public String getGuid() {
        return guid;
    }

    @Override
    public byte[] getDataBytes() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CloudStorageObject)) return false;
        CloudStorageObject that = (CloudStorageObject) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(guid, that.guid) &&
                Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(url, guid);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
