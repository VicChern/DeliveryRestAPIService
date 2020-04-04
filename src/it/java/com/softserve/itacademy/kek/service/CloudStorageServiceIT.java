package com.softserve.itacademy.kek.service;

import java.io.FileInputStream;
import java.util.List;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.testing.RemoteStorageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.services.ICloudStorageObject;
import com.softserve.itacademy.kek.services.impl.CloudStorageServiceImpl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Integration tests for {@link CloudStorageServiceImpl}
 */
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class CloudStorageServiceIT extends AbstractTestNGSpringContextTests {
    @Autowired
    private CloudStorageServiceImpl cloudStorageService;

    private RemoteStorageHelper helper;

    private Storage storage;
    private ICloudStorageObject cloudStorageObject;

    private String bucketName;
    private Bucket bucket;
    private Blob blob;

    private byte[] data;

    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() throws Exception {
        cloudStorageService = new CloudStorageServiceImpl();

        helper = RemoteStorageHelper.create("kinda-express-king-266805", new FileInputStream(System.getenv("GOOGLE_CLOUD_STORAGE_KEY")));
        storage = helper.getOptions().getService();

        bucketName = "kek_default_bucket";
        bucket = storage.get(bucketName);

        data = new byte[]{1, 2, 4, 5};
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() throws Exception {
        storage.delete(blob.getBlobId());
    }

    @Test(groups = {"integration-tests"})
    public void uploadBinaryData() throws Exception {
        cloudStorageObject = cloudStorageService.uploadBinaryData(data);

        blob = bucket.get(cloudStorageObject.getGuid());

        assertNotNull(cloudStorageObject.getUrlString());
        assertNotNull(cloudStorageObject.getGuid());
        assertEquals(data, cloudStorageObject.getDataBytes());
    }

    @Test(groups = {"integration-tests"})
    public void getCloudStorageObject() throws Exception {
        ICloudStorageObject uploadedObject = cloudStorageService.uploadBinaryData(data);
        ICloudStorageObject cloudStorageObject = cloudStorageService.getCloudStorageObject(uploadedObject.getGuid());

        blob = bucket.get(uploadedObject.getGuid());

        assertEquals(cloudStorageObject.getUrlString(), uploadedObject.getUrlString());
        assertEquals(cloudStorageObject.getGuid(), uploadedObject.getGuid());
        assertEquals(data, cloudStorageObject.getDataBytes());
    }

    @Test(groups = {"integration-tests"})
    public void getCloudStorageObjects() throws Exception {
        List<ICloudStorageObject> cloudStorageObjectList = cloudStorageService.getCloudStorageObjects("kek_default_bucket");

        for (ICloudStorageObject object : cloudStorageObjectList) {

            assertNotNull(object.getUrlString());
            assertNotNull(object.getGuid());
            assertNotNull(object.getDataBytes());
        }
    }
}

