package com.softserve.itacademy.kek.controller;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.models.services.ICloudStorageObject;
import com.softserve.itacademy.kek.models.services.impl.CloudStorageObject;
import com.softserve.itacademy.kek.services.ICloudStorageService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Test(groups = {"unit-tests"})
public class CloudStorageControllerTest {

    @InjectMocks
    private CloudStorageController controller;
    @Spy
    private ICloudStorageService cloudStorageService;

    private ICloudStorageObject cloudStorageObject;

    private MockMvc mockMvc;

    private String image;
    private byte[] encodedByte;
    private String url;
    private final String guid = UUID.randomUUID().toString();


    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        image = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==";
        encodedByte = Base64.decodeBase64(image.getBytes());
        url = "pic url";

        cloudStorageObject = new CloudStorageObject(url, guid, encodedByte);
    }

    @Test
    public void addDataTest() throws Exception {
        when(cloudStorageService.uploadBinaryData(any(encodedByte.getClass()))).thenReturn(cloudStorageObject);

        MvcResult result = mockMvc.perform(post("/users/cloud")
                .content(image))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals("url", "pic url", result.getResponse().getContentAsString());
    }

    @Test
    public void modifyDataTest() throws Exception {
        when(cloudStorageService.updateBinaryData(any(guid.getClass()), any(encodedByte.getClass())))
                .thenReturn(cloudStorageObject);

        MvcResult result = mockMvc.perform(put("/users/cloud/820671c6-7e2c-4de3-aeb8-42e6f84e6371")
                .content(image))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("url", "pic url", result.getResponse().getContentAsString());
    }

    @Test
    public void deleteDataTest() throws Exception {
        mockMvc.perform(delete("/users/cloud/820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(status().isAccepted());
    }
}
