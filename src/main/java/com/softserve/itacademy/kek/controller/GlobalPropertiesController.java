package com.softserve.itacademy.kek.controller;

import com.softserve.itacademy.kek.dto.GlobalPropertiesDto;
import com.softserve.itacademy.kek.dto.PropertyTypeDto;
import com.softserve.itacademy.kek.models.IGlobalProperties;
import com.softserve.itacademy.kek.services.IGlobalPropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

