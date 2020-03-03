package com.softserve.itacademy.kek.services;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.services.impl.CloudStorageService;
import com.softserve.itacademy.kek.services.model.ICloudStorageObject;
import com.softserve.itacademy.kek.services.model.impl.CloudStorageObject;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Unit tests for {@link CloudStorageService}
 */
@Test(groups = {"unit-tests"})
public class CloudStorageServiceTest {

    private ICloudStorageService cloudStorageService;

    private String bucket;

    private byte[] data;
    private String guid;
    private String url;

    private CloudStorageObject cloudStorageObject;
    private List<ICloudStorageObject> objectList;

    @BeforeClass
    public void setUp() {
        cloudStorageService = new CloudStorageService("storage.properties");

        bucket = "kek_default_bucket";

        data = new byte[]{1, 2, 4, 5};
        guid = "274d9fbb-4f47-42f7-8fcb-2485be10c8af";
        url = "https://www.googleapis.com/download/storage/v1/b/kek_default_bucket/o/274d9fbb-4f47-42f7-8fcb-2485be10c8af?generation=1583261105544890&alt=media";
        cloudStorageObject = new CloudStorageObject(url, guid, data);

        objectList = new ArrayList<>();
        objectList.add(cloudStorageObject);

    }

    @Test
    public void uploadBinaryData() throws Exception {
        ICloudStorageObject createdObject = cloudStorageService.uploadBinaryData(data);

        assertNotNull(createdObject);

        assertNotNull(createdObject.getUrlString());
        assertNotNull(createdObject.getGuid());
        assertEquals(data, createdObject.getDataBytes());
    }

    @Test
    public void getCloudStorageObject() throws Exception {
        ICloudStorageObject newCloudStorageObject = cloudStorageService.getCloudStorageObject(guid);

        assertNotNull(newCloudStorageObject);
//
        assertEquals(cloudStorageObject.getUrlString(), newCloudStorageObject.getUrlString());
        assertEquals(cloudStorageObject.getGuid(), newCloudStorageObject.getGuid());
        assertEquals(cloudStorageObject.getDataBytes(), newCloudStorageObject.getDataBytes());
    }

    @Test
    public void getCloudStorageObjects() throws Exception {
        List<ICloudStorageObject> newCloudStorageObjectList = cloudStorageService.getCloudStorageObjects(bucket);

        for (ICloudStorageObject object : newCloudStorageObjectList) {
            assertNotNull(object);

            assertNotNull(object.getUrlString());
            assertNotNull(object.getGuid());
            assertNotNull(object.getDataBytes());
        }
    }
}

