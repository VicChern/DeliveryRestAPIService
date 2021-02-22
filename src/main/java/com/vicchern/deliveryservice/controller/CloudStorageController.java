package com.vicchern.deliveryservice.controller;

import com.vicchern.deliveryservice.controller.utils.DeliveryServiceMappingValues;
import com.vicchern.deliveryservice.controller.utils.DeliveryServicePaths;
import com.vicchern.deliveryservice.controller.utils.DeliveryServiceRoles;
import com.vicchern.deliveryservice.models.services.ICloudStorageObject;
import com.vicchern.deliveryservice.services.ICloudStorageService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = DeliveryServicePaths.USER_CLOUD)
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
    @PreAuthorize(DeliveryServiceRoles.USER_ADMIN)
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
    @PutMapping(value = DeliveryServiceMappingValues.GUID)
    @PreAuthorize(DeliveryServiceRoles.USER_ADMIN)
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
    @DeleteMapping(DeliveryServiceMappingValues.GUID)
    @PreAuthorize(DeliveryServiceRoles.TENANT_USER_ADMIN)
    public ResponseEntity deleteData(@PathVariable String guid) {
        logger.info("Accepted request to delete the data from Google Cloud Storage bucket by guid {}", guid);

        cloudStorageService.deleteByGuid(guid);

        logger.info("Data ({}) successfully deleted", guid);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }
}
