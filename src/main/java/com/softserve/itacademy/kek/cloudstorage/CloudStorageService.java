package com.softserve.itacademy.kek.cloudstorage;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.softserve.itacademy.kek.exception.CloudStorageServiceException;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CloudStorageService implements ICloudStorageService {

    @Override
    public CloudStorageObject uploadBinaryData(byte[] data) throws CloudStorageServiceException {
        String bucket = System.getenv("BUCKET_NAME");
        return uploadBinaryData(data, bucket);
    }

    public CloudStorageObject uploadBinaryData(byte[] data, String bucketName) throws CloudStorageServiceException {
        Storage storage = getStorageObject();
        Bucket bucket = null;

        if (storage.get(bucketName) != null) {
            bucket = storage.update(BucketInfo.of(bucketName));
        } else {
            bucket = storage.create(BucketInfo.of(bucketName));
        }
        String guid = UUID.randomUUID().toString();
        Blob blob = bucket.create(guid, data);
        String url = blob.getMediaLink();
        return new CloudStorageObject(url, guid, data);
    }

    @Override
    public CloudStorageObject getCloudStorageObject(String guid) throws CloudStorageServiceException {
        String bucket = System.getenv("BUCKET_NAME");
        return getCloudStorageObject(guid, bucket);
    }

    public CloudStorageObject getCloudStorageObject(String guid, String bucketName) throws CloudStorageServiceException {
        Storage storage = getStorageObject();

        if (storage.get(bucketName) != null) {
            return null;
        } else {
            Bucket bucket = storage.update(BucketInfo.of(bucketName));
            Blob blob = bucket.get(guid);
            byte[] data = blob.getContent();
            String url = blob.getMediaLink();
            return new CloudStorageObject(url, guid, data);
        }
    }

    @Override
    public List<CloudStorageObject> getCloudStorageObjects(String filter) throws CloudStorageServiceException {
        List<CloudStorageObject> objects = new ArrayList<>();

        Storage storage = getStorageObject();
        Bucket bucket = storage.update(BucketInfo.of(filter));

        Page<Blob> blobs = bucket.list();

        for (Blob blob : blobs.getValues()) {
            String url = blob.getMediaLink();
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
            Object obj = new JSONParser().parse(new FileReader(token)); //parses token and gets projectID
            JSONObject jo = (JSONObject) obj;
            String projectID = (String) jo.get("project_id");

            storageOptions = StorageOptions.newBuilder()
                    .setProjectId(projectID)
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream(token)))
                    .build();
        } catch (IOException e) {
            throw new CloudStorageServiceException("Token was not found");
        } catch (ParseException e) {
            throw new CloudStorageServiceException("Token was not parsed");
        }

        Storage storage = storageOptions.getService();
        return storage;
    }
}
