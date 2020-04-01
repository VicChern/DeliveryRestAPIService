package com.softserve.itacademy.kek.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.services.ICloudStorageService;

@RestController
@RequestMapping(path = "/users/cloud")
public class CloudStorageController {
    private final Logger logger = LoggerFactory.getLogger(CloudStorageController.class);

    private final ICloudStorageService cloudStorageService;

    @Autowired
    public CloudStorageController(ICloudStorageService cloudStorageService) {
        this.cloudStorageService = cloudStorageService;
    }

    @GetMapping(value = KekMappingValues.GUID, produces = KekMediaType.USER)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getData(@PathVariable String guid) {
        return null;
    }

    @GetMapping(produces = KekMediaType.USER_LIST)
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity getDataList() {
        return null;
    }

    @PostMapping(consumes = KekMediaType.USER, produces = KekMediaType.USER)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity uploadData(@RequestBody @Valid UserDto newUserDto) {
        return null;
    }
}
