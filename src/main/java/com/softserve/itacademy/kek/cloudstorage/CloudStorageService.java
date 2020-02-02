package com.softserve.itacademy.kek.cloudstorage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;


public class CloudStorageService implements ICloudStorageService {
    @Override
    public CloudStorageObject uploadBinaryData(byte[] data) throws CloudStorageServiceException {

        Storage storage = getStorageObject();
        Bucket bucket = storage.update(BucketInfo.of("kek-default-bucket"));

        String guid = UUID.randomUUID().toString();
        Blob blob = bucket.create(guid, data);

        String url = "gs://" + bucket.getName() + "/" + blob.getName();
        return new CloudStorageObject(url, guid, data);
    }

    public CloudStorageObject uploadBinaryData(byte[] data, String bucketName) throws CloudStorageServiceException {

        Storage storage = getStorageObject();
        Bucket bucket = null;

        if (storage.get(bucketName) != null){
            bucket = storage.update(BucketInfo.of(bucketName));
        } else {
            bucket = storage.create(BucketInfo.of(bucketName));
        }

        String guid = UUID.randomUUID().toString();
        Blob blob = bucket.create(guid, data);

        String url = "https://storage.cloud.google.com/" + bucket.getName() + "/" + blob.getName() + "?authuser=1";
        return new CloudStorageObject(url, guid, data);
    }

    @Override
    public CloudStorageObject getCloudStorageObject(String guid) throws CloudStorageServiceException {
        Storage storage = getStorageObject();
        Bucket bucket = storage.update(BucketInfo.of("kek-default-bucket"));
        Blob blob = bucket.get(guid);
        byte[] data = blob.getContent();
        String url = "https://storage.cloud.google.com/" + bucket.getName() + "/" + guid + "?authuser=1";
        return new CloudStorageObject(url, guid, data);
    }

    public CloudStorageObject getCloudStorageObject(String guid, String bucketName) throws CloudStorageServiceException {
        Storage storage = getStorageObject();
        Bucket bucket = storage.update(BucketInfo.of(bucketName));
        Blob blob = bucket.get(guid);
        byte[] data = blob.getContent();
        String url = "https://storage.cloud.google.com/" + bucket.getName() + "/" + guid + "?authuser=1";
        return new CloudStorageObject(url, guid, data);
    }

    @Override
    public List<CloudStorageObject> getCloudStorageObjects(String filter) throws CloudStorageServiceException {
        List<CloudStorageObject> objects = new ArrayList<>();

        Storage storage = getStorageObject();
        Bucket bucket = storage.update(BucketInfo.of(filter));

        Page<Blob> blobs = bucket.list();

        for (Blob blob : blobs.getValues()){
            String url = "https://storage.cloud.google.com/" + filter + "/" + blob.getName() + "?authuser=1";
            String guid = blob.getName();
            byte[] data = blob.getContent();
            objects.add(new CloudStorageObject(url, guid, data));
        }

        return objects;
    }

    public Storage getStorageObject() {
        StorageOptions storageOptions = null;
        String token = System.getenv("GOOGLE_CLOUD_STORAGE_KEY");
        try {
            storageOptions = StorageOptions.newBuilder()
                    .setProjectId("kinda-express-king-266805")
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream(token)))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Storage storage = storageOptions.getService();
        return storage;
    }
}
