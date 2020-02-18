package com.softserve.itacademy.kek.service.impl;


import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.softserve.itacademy.kek.exception.CloudStorageServiceException;
import com.softserve.itacademy.kek.service.model.impl.CloudStorageObject;
import com.softserve.itacademy.kek.service.ICloudStorageService;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;


public class CloudStorageService implements ICloudStorageService {
    private static final Logger logger = Logger.getLogger(CloudStorageService.class);

    @Override
    public CloudStorageObject uploadBinaryData(final byte[] data) throws CloudStorageServiceException {
        logger.debug("Uploading binary data to Google Cloud Storage default bucket");

        final String bucket = getBucketName("storage.properties");

        return uploadBinaryData(data, bucket);
    }

    public CloudStorageObject uploadBinaryData(final byte[] data, final String bucketName) throws CloudStorageServiceException {
        logger.debug("Uploading binary data to Google Cloud Storage bucket");

        final Storage storage = getStorageObject();
        final Bucket bucket;

        if (storage.get(bucketName) != null) {
            bucket = storage.update(BucketInfo.of(bucketName));
        } else {
            bucket = storage.create(BucketInfo.of(bucketName));
            logger.debug("Google Cloud Storage bucket created successfully");
        }

        final String guid = UUID.randomUUID().toString();
        final Blob blob = bucket.create(guid, data);
        final String url = blob.getMediaLink();

        return new CloudStorageObject(url, guid, data);
    }

    @Override
    public CloudStorageObject getCloudStorageObject(final String guid) throws CloudStorageServiceException {
        logger.debug("Getting object by GUID from Google Cloud Storage default bucket");

        final String bucket = getBucketName("storage.properties");

        return getCloudStorageObject(guid, bucket);
    }

    public CloudStorageObject getCloudStorageObject(final String guid, final String bucketName) throws CloudStorageServiceException {
        logger.debug("Getting object by GUID from Google Cloud Storage bucket");

        final Storage storage = getStorageObject();

        final Bucket bucket = storage.update(BucketInfo.of(bucketName));
        final Blob blob = bucket.get(guid);
        final byte[] data = blob.getContent();
        final String url = blob.getMediaLink();

        return new CloudStorageObject(url, guid, data);
    }

    @Override
    public List<CloudStorageObject> getCloudStorageObjects(final String filter) throws CloudStorageServiceException {
        logger.debug("Getting list of objects from Google Cloud Storage bucket");

        final List<CloudStorageObject> objects = new ArrayList<>();

        final Storage storage = getStorageObject();
        final Bucket bucket = storage.update(BucketInfo.of(filter));

        final Page<Blob> blobs = bucket.list();

        for (Blob blob : blobs.getValues()) {

            String url = blob.getMediaLink();
            String guid = blob.getName();
            byte[] data = blob.getContent();
            objects.add(new CloudStorageObject(url, guid, data));
        }

        return objects;
    }

    public Storage getStorageObject() {
        logger.debug("Creating storage object");

        final StorageOptions storageOptions;
        final String projectID;
        final Storage storage;

        final String token = System.getenv("GOOGLE_CLOUD_STORAGE_KEY");

        try {
            Object obj = new JSONParser().parse(new FileReader(token));
            JSONObject jo = (JSONObject) obj;
            projectID = (String) jo.get("project_id");

            logger.debug("Token parsed successfully");

        } catch (Exception ex) {
            logger.warn("Token was not parsed", ex);

            throw new CloudStorageServiceException(ex);
        }

        try {
            storageOptions = StorageOptions.newBuilder()
                    .setProjectId(projectID)
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream(token)))
                    .build();
            storage = storageOptions.getService();

            logger.debug("Storage object created successfully");

        } catch (Exception ex) {
            logger.warn("Token was not found", ex);

            throw new CloudStorageServiceException(ex);
        }

        return storage;
    }

    public String getBucketName(final String propFileName) {
        logger.debug("Getting name of Google Cloud Storage default bucket");

        final String bucketName;
        final Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            logger.debug("Getting bucket name form .properties");

            properties.load(inputStream);
            bucketName = properties.getProperty("bucketName");


        } catch (Exception ex) {
            logger.warn("Bucket name was not initialized", ex);

            throw new CloudStorageServiceException(ex);

        }

        return bucketName;
    }
}