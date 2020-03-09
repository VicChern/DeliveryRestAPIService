package com.softserve.itacademy.kek.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.testing.RemoteStorageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.services.impl.CloudStorageService;
import com.softserve.itacademy.kek.services.model.ICloudStorageObject;
import com.softserve.itacademy.kek.services.model.impl.CloudStorageObject;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Integration tests for {@link CloudStorageService}
 */
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class CloudStorageServiceIT extends AbstractTestNGSpringContextTests {
    @Autowired
    private CloudStorageService cloudStorageService;

    private RemoteStorageHelper helper;
    private Storage storage;

    private String bucketName;
    private Bucket bucket;

    private byte[] data;
    private String guid;

    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() throws FileNotFoundException {
        helper = RemoteStorageHelper.create("kinda-express-king-266805", new FileInputStream(System.getenv("GOOGLE_CLOUD_STORAGE_KEY")));

        storage = helper.getOptions().getService();
        bucketName = RemoteStorageHelper.generateBucketName();

        data = new byte[]{1, 2, 4, 5};
        guid = "274d9fbb-4f47-42f7-8fcb-2485be10c8af";


    }

    @Test(groups = {"integration-tests"})
    public void uploadBinaryData() throws Exception {
        bucket = storage.create(BucketInfo.of(bucketName));
        Blob blob = bucket.create(String.valueOf(guid), data);

        String urlFromCloud = blob.getMediaLink();
        byte[] dataFromCloud = blob.getContent();
        String guidFromCloud = blob.getName();

        assertNotNull(urlFromCloud);
        assertEquals(String.valueOf(guid), guidFromCloud);
        assertEquals(data, dataFromCloud);
    }

    @Test(groups = {"integration-tests"})
    public void getCloudStorageObject() throws Exception {
        uploadBinaryData();

        bucket = storage.get(bucketName);
        Blob blob = bucket.get(String.valueOf(guid));

        String urlFromCloud = blob.getMediaLink();
        byte[] dataFromCloud = blob.getContent();
        String guidFromCloud = blob.getName();

        assertNotNull(urlFromCloud);
        assertNotNull(guidFromCloud);
        assertNotNull(dataFromCloud);
    }

    @Test(groups = {"integration-tests"})
    public void getCloudStorageObjects() throws Exception {
        uploadBinaryData();

        bucket = storage.get(bucketName);

        final List<ICloudStorageObject> result = StreamSupport.stream(bucket.list().getValues().spliterator(), false)
                .map(blob -> {
                    final String url = blob.getMediaLink();
                    final String guid = blob.getName();
                    final byte[] data = blob.getContent();

                    return new CloudStorageObject(url, guid, data);
                })
                .collect(Collectors.toList());

        for (ICloudStorageObject object : result) {
            assertNotNull(object);

            assertNotNull(object.getUrlString());
            assertNotNull(object.getGuid());
            assertNotNull(object.getDataBytes());
        }
    }
}

