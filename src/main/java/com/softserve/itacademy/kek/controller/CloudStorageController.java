package com.softserve.itacademy.kek.controller;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.controller.utils.KekMappingValues;
import com.softserve.itacademy.kek.models.services.ICloudStorageObject;
import com.softserve.itacademy.kek.services.ICloudStorageService;

@RestController
@RequestMapping(path = "/users/cloud")
public class CloudStorageController {
    private final static Logger logger = LoggerFactory.getLogger(CloudStorageController.class);

    private final ICloudStorageService cloudStorageService;

    @Autowired
    public CloudStorageController(ICloudStorageService cloudStorageService) {
        this.cloudStorageService = cloudStorageService;
    }

    /**
     * Uploads byte array to Cloud Storage
     *
     * @param image from user
     * @return url to uploaded data in Cloud Storage
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> addData(@RequestBody String image) {
        logger.info("Accepted requested to upload data to Cloud Storage for user");

        byte[] encodedByte = Base64.decodeBase64(image.getBytes());

        ICloudStorageObject cloudStorageObject = cloudStorageService.uploadBinaryData(encodedByte);

        String url = cloudStorageObject.getUrlString();

        logger.info("Sending the url to uploaded data in Cloud Storage:\n{}", url);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(url);
    }

    /**
     * Modifies data in Cloud Storage
     *
     * @param guid  user guid from the URN
     * @param image from user
     * @return url to modified data in Cloud Storage
     */
    @PutMapping(value = KekMappingValues.GUID)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> modifyData(@PathVariable String guid, @RequestBody String image) {
        logger.info("Accepted modified data from the client ");

        byte[] encodedByte = Base64.decodeBase64(image.getBytes());

        ICloudStorageObject cloudStorageObject = cloudStorageService.updateBinaryData(guid, encodedByte);

        String modifiedUrl = cloudStorageObject.getUrlString();

        logger.info("Sending the modified url to the client:\n{}", modifiedUrl);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modifiedUrl);
    }

    /**
     * Removes the data from Cloud Storage
     *
     * @param guid blob guid from the URN
     */
    @DeleteMapping(KekMappingValues.GUID)
    @PreAuthorize("hasRole('TENANT') or hasRole('USER')")
    public ResponseEntity deleteData(@PathVariable String guid) {
        logger.info("Accepted request to delete the data from Google Cloud Storage bucket by guid {}", guid);

        cloudStorageService.deleteByGuid(guid);

        logger.info("Data ({}) successfully deleted", guid);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }
}
