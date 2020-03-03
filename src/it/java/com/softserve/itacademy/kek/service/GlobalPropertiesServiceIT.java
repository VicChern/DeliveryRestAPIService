package com.softserve.itacademy.kek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.impl.GlobalProperties;
import com.softserve.itacademy.kek.models.impl.PropertyType;
import com.softserve.itacademy.kek.repositories.GlobalPropertiesRepository;
import com.softserve.itacademy.kek.repositories.PropertyTypeRepository;
import com.softserve.itacademy.kek.services.IGlobalPropertiesService;

import static org.testng.Assert.assertEquals;

@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class GlobalPropertiesServiceIT extends AbstractTestNGSpringContextTests {

    public static final String SCHEMA = "schema";
    public static final String VALUE = "value";
    public static final String NEW_VALUE = "new value";
    public static final String NAME = "name";
    public static final String NEW_NAME = "new name";
    public static final String KEY = "key";

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;
    @Autowired
    private GlobalPropertiesRepository globalPropertiesRepository;

    @Autowired
    private IGlobalPropertiesService globalPropertiesService;

    @AfterMethod
    public void tearDown() {
        globalPropertiesRepository.deleteAll();
        propertyTypeRepository.deleteAll();
    }

    @Test
    public void createSuccess() {
        //when
        final PropertyType propertyType = new PropertyType();

        propertyType.setName(NAME);
        propertyType.setSchema(SCHEMA);

        final PropertyType savedPropertyType = propertyTypeRepository.save(propertyType);

        GlobalProperties globalProperties = new GlobalProperties();

        globalProperties.setValue(VALUE);
        globalProperties.setKey(KEY);
        globalProperties.setPropertyType(savedPropertyType);

        globalPropertiesService.create(globalProperties);
        //then
        GlobalProperties foundGlobalProperties = globalPropertiesRepository.findByKey(globalProperties.getKey());

        assertEquals(globalProperties.getKey(), foundGlobalProperties.getKey());
        assertEquals(globalProperties.getValue(), foundGlobalProperties.getValue());
        assertEquals(globalProperties.getPropertyType(), foundGlobalProperties.getPropertyType());
    }

    @Test
    public void updateSuccess() {
        //when
        final PropertyType propertyType = new PropertyType();

        propertyType.setName(NAME);
        propertyType.setSchema(SCHEMA);

        GlobalProperties globalProperties = new GlobalProperties();

        globalProperties.setValue(VALUE);
        globalProperties.setKey(KEY);
        globalProperties.setPropertyType(propertyType);

        globalPropertiesRepository.save(globalProperties);

        globalProperties.setValue(NEW_VALUE);
        propertyType.setName(NEW_NAME);

        globalProperties.setPropertyType(propertyType);

        globalPropertiesService.update(globalProperties, KEY, NAME);
        //then
        GlobalProperties foundGlobalProperties = globalPropertiesRepository.findByKey(globalProperties.getKey());

        assertEquals(globalProperties.getKey(), foundGlobalProperties.getKey());
        assertEquals(globalProperties.getValue(), foundGlobalProperties.getValue());
        assertEquals(globalProperties.getPropertyType(), foundGlobalProperties.getPropertyType());

    }
}
