package com.softserve.itacademy.kek.controller;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.controller.utils.KekMappingValues;
import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.dto.DetailsDto;
import com.softserve.itacademy.kek.dto.ListWrapperDto;
import com.softserve.itacademy.kek.dto.UserDetailsWithImageDto;
import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.dto.UserWithImageDto;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.services.ICloudStorageObject;
import com.softserve.itacademy.kek.services.ICloudStorageService;
import com.softserve.itacademy.kek.services.IUserService;

@RestController
@RequestMapping(path = "/users/cloud")
public class CloudStorageController {
    private final static Logger logger = LoggerFactory.getLogger(CloudStorageController.class);

    private final ICloudStorageService cloudStorageService;
    private final IUserService userService;

    @Autowired
    public CloudStorageController(ICloudStorageService cloudStorageService, IUserService userService) {
        this.cloudStorageService = cloudStorageService;
        this.userService = userService;
    }

    /**
     * Gets stored data from Google Cloud Storage bucket by GUID
     *
     * @param guid user ID from the URN
     * @return Response entity with {@link UserWithImageDto} object as a JSON
     */
    @GetMapping(value = KekMappingValues.GUID, produces = KekMediaType.USER_WITH_IMAGE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserWithImageDto> getData(@PathVariable String guid) {
        logger.info("Client requested the data from Cloud Storage for user:\n{}", guid);

        ICloudStorageObject cloudStorageObject = cloudStorageService.getCloudStorageObject(guid);

        IUser user = userService.getByGuid(UUID.fromString(guid));

        UserWithImageDto userWithImageDto = transformUser(user, cloudStorageObject);

        logger.info("Sending the user with data from Cloud Storage to the client:\n{}", userWithImageDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userWithImageDto);
    }

    /**
     * Gets list of stored objects from Google Cloud Storage bucket
     *
     * @param bucket bucket name from the URN
     * @return Response entity with list of {@link UserWithImageDto} object as a JSON
     */
    @GetMapping(value = KekMappingValues.BUCKET, produces = KekMediaType.USER_WITH_IMAGE)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<ListWrapperDto<UserWithImageDto>> getDataList(@PathVariable String bucket) {
        logger.info("Client requested the list of data from Cloud Storage which stored in bucket:\n{}", bucket);

        List<ICloudStorageObject> dataList = cloudStorageService.getCloudStorageObjects(bucket);

        ListWrapperDto<UserWithImageDto> users = new ListWrapperDto<>();

        for (ICloudStorageObject cloudStorageObject : dataList) {
            IUser user = userService.getByGuid(UUID.fromString(cloudStorageObject.getGuid()));

            users.addKekItem(transformUser(user, cloudStorageObject));
        }

        logger.info("Sending the list of users with data from Cloud Storage to the client:\n{}", users);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    /**
     * Uploads byte array to Google Cloud Storage
     *
     * @param userWithImage {@link UserWithImageDto} object as a JSON
     * @return Response entity with {@link UserDto} object as a JSON
     */
    @PostMapping(consumes = KekMediaType.USER_WITH_IMAGE, produces = KekMediaType.USER)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> uploadData(@RequestBody @Valid UserWithImageDto userWithImage) {
        logger.info("Accepted requested to upload data to Cloud Storage for user:\n{}", userWithImage);

        UserDetailsWithImageDto userDetails = userWithImage.getUserDetailsWithImage();
        String data = userDetails.getImage();

        byte[] encodedByte = Base64.decodeBase64(data.getBytes());

        ICloudStorageObject cloudStorageObject = cloudStorageService.uploadBinaryData(encodedByte, userWithImage.getGuid());
        String url = cloudStorageObject.getUrlString();

        UserDto userDto = transformUser(userWithImage, url);

        logger.info("Sending the user with uploaded data to Cloud Storage:\n{}", userDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto);
    }

    private UserDto transformUser(UserWithImageDto userWithImage, String url) {
        DetailsDto userDetailsDto = new DetailsDto(url, userWithImage.getUserDetailsWithImage().getPayload());

        return new UserDto(userWithImage.getGuid(), userWithImage.getName(), userWithImage.getNickname(),
                userWithImage.getEmail(), userWithImage.getPhoneNumber(), userDetailsDto);
    }

    private UserWithImageDto transformUser(IUser user, ICloudStorageObject cloudStorageObject) {
        UserDetailsWithImageDto userDetails = new UserDetailsWithImageDto(user.getUserDetails().getPayload(),
                Base64.encodeBase64String(cloudStorageObject.getDataBytes()));

        return new UserWithImageDto(user.getGuid(), user.getName(), user.getNickname(),
                user.getEmail(), user.getPhoneNumber(), userDetails);
    }
}
