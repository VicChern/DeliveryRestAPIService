package com.softserve.itacademy.kek.controller;

import com.softserve.itacademy.kek.dto.GlobalPropertiesDto;
import com.softserve.itacademy.kek.dto.GlobalPropertiesListDto;
import com.softserve.itacademy.kek.dto.PropertyTypeDto;
import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.models.IGlobalProperties;
import com.softserve.itacademy.kek.services.IGlobalPropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class GlobalPropertiesController {
    final static Logger logger = LoggerFactory.getLogger(GlobalPropertiesController.class);

    private final IGlobalPropertiesService globalPropertiesService;

    @Autowired
    public GlobalPropertiesController(IGlobalPropertiesService globalPropertiesService) {
        this.globalPropertiesService = globalPropertiesService;
    }

    /**
     * Transform {@link IGlobalProperties} to {@link GlobalPropertiesDto}
     *
     * @param globalProperties globalProperties
     * @return tenantDto
     */
    private GlobalPropertiesDto transformGlobalProperty(IGlobalProperties globalProperties) {
        PropertyTypeDto propertyType = new PropertyTypeDto(
                globalProperties.getPropertyType().getName(),
                globalProperties.getPropertyType().getSchema());

        GlobalPropertiesDto globalPropertiesDto = new GlobalPropertiesDto(
                propertyType,
                globalProperties.getKey(),
                globalProperties.getValue());

        return globalPropertiesDto;
    }

    /**
     * Get information about tenants
     *
     * @return Response Entity with a list of {@link GlobalPropertiesListDto} objects as a JSON
     */
    @GetMapping(produces = "application/vnd.softserve.globalPropertyList+json")
    public ResponseEntity <GlobalPropertiesListDto> getGlobalPropertiesList (){
        logger.info("Client requested the list of all tenants");

        List<IGlobalProperties> globalPropertiesList = globalPropertiesService.getAll();
        GlobalPropertiesListDto globalPropertiesListDto = new GlobalPropertiesListDto(globalPropertiesList
                .stream()
                .map(this::transformGlobalProperty)
                .collect(Collectors.toList()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(globalPropertiesListDto);
    }

    /**
     * Creates a new global property
     *
     * @param globalPropertiesDto {@link GlobalPropertiesDto} object as a JSON
     * @return Response Entity with {@link GlobalPropertiesDto} object as a JSON
     */
    @PostMapping(consumes = "application/vnd.softserve.globalProperty+json",produces = "application/vnd.softserve.globalProperty+json")
    public ResponseEntity <GlobalPropertiesDto> addGlobalProperty(@RequestBody @Valid GlobalPropertiesDto globalPropertiesDto){
        logger.info("Accepted requested to create a new global property:\n{}", globalPropertiesDto);

        IGlobalProperties globalProperties = globalPropertiesService.create(globalPropertiesDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transformGlobalProperty(globalProperties));
    }

}

