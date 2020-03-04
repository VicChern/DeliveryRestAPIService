package com.softserve.itacademy.kek.services.impl;


import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.softserve.itacademy.kek.exception.CloudStorageServiceException;
import com.softserve.itacademy.kek.services.AbstractService;
import com.softserve.itacademy.kek.services.ICloudStorageService;
import com.softserve.itacademy.kek.services.model.ICloudStorageObject;
import com.softserve.itacademy.kek.services.model.impl.CloudStorageObject;

@Component
public class CloudStorageService extends AbstractService implements ICloudStorageService {
    private static final Logger logger = LoggerFactory.getLogger(CloudStorageService.class);

    @Value("gcp.storage.filename")
    private String storagePropertiesFileName;

    @Override
    public ICloudStorageObject uploadBinaryData(final byte[] data) throws CloudStorageServiceException {
        logger.info("Uploading binary data to Google Cloud Storage default bucket");

        try {
            final String bucket = getBucketName();
            return uploadBinaryData(data, bucket);
        } catch (Exception ex) {
            logger.error("Can't uploadBinaryData; data = " + data, ex);
            throw new CloudStorageServiceException(ex);
        }
    }

    ICloudStorageObject uploadBinaryData(final byte[] data, final String bucketName) throws Exception {
        logger.debug("Uploading binary data to Google Cloud Storage bucket; bucketName = {}", bucketName);

        final Storage storageConnection = getStorageObject();
        final Bucket bucket;

        if (storageConnection.get(bucketName) != null) {
            bucket = storageConnection.update(BucketInfo.of(bucketName));
            logger.debug("Google Cloud Storage bucket updated successfully; bucketName = {}", bucketName);
        } else {
            bucket = storageConnection.create(BucketInfo.of(bucketName));
            logger.debug("Google Cloud Storage bucket created successfully; bucketName = {}", bucketName);
        }

        final String guid = UUID.randomUUID().toString();
        final Blob blob = bucket.create(guid, data);
        final String url = blob.getMediaLink();

        return new CloudStorageObject(url, guid, data);
    }

    @Override
    public ICloudStorageObject getCloudStorageObject(final String guid) throws CloudStorageServiceException {
        logger.info("Getting object by GUID from Google Cloud Storage default bucket; guid = {}", guid);

        try {
            final String bucket = getBucketName();
            return getCloudStorageObject(guid, bucket);
        } catch (Exception ex) {
            logger.error("Can't getCloudStorageObject; guid = " + guid, ex);
            throw new CloudStorageServiceException(ex);
        }
    }

    ICloudStorageObject getCloudStorageObject(final String guid, final String bucketName) throws Exception {
        logger.debug("Getting object by GUID from Google Cloud Storage bucket; bucketName = {}, guid = {}", bucketName, guid);

        final Storage storageConnection = getStorageObject();

        final Bucket bucket = storageConnection.get(bucketName);
        final Blob blob = bucket.get(guid);
        final byte[] data = blob.getContent();
        final String url = blob.getMediaLink();

        return new CloudStorageObject(url, guid, data);
    }

    @Override
    public List<ICloudStorageObject> getCloudStorageObjects(final String filter) throws CloudStorageServiceException {
        logger.info("Getting list of objects from Google Cloud Storage bucket; filter = {}", filter);
        try {
            final String bucketName = getBucketName();
            return getCloudStorageObjects(bucketName, filter);
        } catch (Exception ex) {
            logger.error("Can't getCloudStorageObjects; filter = " + filter, ex);
            throw new CloudStorageServiceException(ex);
        }
    }

    List<ICloudStorageObject> getCloudStorageObjects(final String bucketName, final String filter) {

        final Storage storageConnection = getStorageObject();
        final Bucket bucket = storageConnection.get(bucketName);

        final List<ICloudStorageObject> result = StreamSupport.stream(bucket.list().getValues().spliterator(), false)
                .map(blob -> {
                    final String url = blob.getMediaLink();
                    final String guid = blob.getName();
                    final byte[] data = blob.getContent();

                    return new CloudStorageObject(url, guid, data);
                })
                .collect(Collectors.toList());

        logger.debug("Collected {} object from bucket = {}, by filter = {}", result.size(), bucketName, filter);

        return result;
    }

    // TODO: refactor this, get rid off global ENV variables
    Storage getStorageObject() {
        logger.debug("Creating storage object");

        final StorageOptions storageOptions;
        final String projectID;
        final Storage storage;

        final String token = System.getenv("GOOGLE_CLOUD_STORAGE_KEY");

        try {
            final Object obj = new JSONParser().parse(new FileReader(token));
            final JSONObject jo = (JSONObject) obj;
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

    // TODO: replace this method with a property service call
    String getBucketName() {
        logger.debug("Getting name of Google Cloud Storage default bucket");

        final String bucketName;
        final Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(storagePropertiesFileName)) {
            logger.debug("Getting bucket name form .properties file {}", storagePropertiesFileName);

            properties.load(inputStream);
            bucketName = properties.getProperty("bucketName");
        } catch (Exception ex) {
            logger.warn("Bucket name was not initialized, can't read it from file = " + storagePropertiesFileName, ex);
            throw new CloudStorageServiceException(ex);
        }

        return bucketName;
    }
}