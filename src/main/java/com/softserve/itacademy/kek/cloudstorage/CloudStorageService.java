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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;


public class CloudStorageService implements ICloudStorageService {
    private static final Logger logger = LoggerFactory.getLogger(CloudStorageService.class);

    @Override
    public CloudStorageObject uploadBinaryData(final byte[] data) throws CloudStorageServiceException {
        logger.info("Uploading binary data to Google Cloud Storage default bucket");
        String bucket = getBucketName("storage.properties");
        return uploadBinaryData(data, bucket);
    }

    public CloudStorageObject uploadBinaryData(final byte[] data, final String bucketName) throws CloudStorageServiceException {
        logger.info("Uploading binary data to Google Cloud Storage bucket");
        Storage storage = getStorageObject();
        Bucket bucket;

        if (storage.get(bucketName) != null) {
            bucket = storage.update(BucketInfo.of(bucketName));
        } else {
            bucket = storage.create(BucketInfo.of(bucketName));
            logger.info("Google Cloud Storage bucket created successfully");
        }
        String guid = UUID.randomUUID().toString();
        Blob blob = bucket.create(guid, data);
        String url = blob.getMediaLink();
        return new CloudStorageObject(url, guid, data);
    }

    @Override
    public CloudStorageObject getCloudStorageObject(final String guid) throws CloudStorageServiceException {
        logger.info("Getting object by GUID from Google Cloud Storage default bucket");
        final String bucket = getBucketName("storage.properties");

        return getCloudStorageObject(guid, bucket);
    }

    public CloudStorageObject getCloudStorageObject(final String guid, final String bucketName) throws CloudStorageServiceException {
        logger.info("Getting object by GUID from Google Cloud Storage bucket");
        Storage storage = getStorageObject();

        if (storage.get(bucketName) != null) {
            logger.info("Bucket does not exist");
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
    public List<CloudStorageObject> getCloudStorageObjects(final String filter) throws CloudStorageServiceException {
        logger.info("Getting list of objects from Google Cloud Storage bucket");
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
        logger.info("Creating storage object");

        StorageOptions storageOptions = null;
        String token = System.getenv("GOOGLE_CLOUD_STORAGE_KEY");
        try {
            logger.info("Token parsed successfully");

            Object obj = new JSONParser().parse(new FileReader(token));
            JSONObject jo = (JSONObject) obj;
            String projectID = (String) jo.get("project_id");

            storageOptions = StorageOptions.newBuilder()
                    .setProjectId(projectID)
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream(token)))
                    .build();
        } catch (IOException e) {
            logger.info("Token was not found");

            return null;
        } catch (ParseException e) {
            logger.info("Token was not parsed");

            return null;
        }

        Storage storage = storageOptions.getService();
        return storage;
    }

    public String getBucketName(final String propFileName) {
        logger.info("Getting name of Google Cloud Storage default bucket");

        String bucketName;

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            logger.info("Getting bucket name form .properties");

            Properties properties = new Properties();

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                logger.info("Property file was not found in the classpath");

                throw new FileNotFoundException("Property file " + propFileName + " was not found in the classpath");
            }
            bucketName = properties.getProperty("bucketName");
        } catch (IOException e) {
            logger.info("Bucket name was not initialized");

            return null;
        }

        return bucketName;
    }
}