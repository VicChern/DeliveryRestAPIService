package com.softserve.itacademy.kek.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.IGlobalProperty;
import com.softserve.itacademy.kek.models.impl.GlobalProperty;
import com.softserve.itacademy.kek.models.impl.PropertyType;
import com.softserve.itacademy.kek.repositories.GlobalPropertiesRepository;
import com.softserve.itacademy.kek.repositories.PropertyTypeRepository;
import com.softserve.itacademy.kek.services.IGlobalPropertiesService;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

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

        GlobalProperty globalProperties = new GlobalProperty();

        globalProperties.setValue(VALUE);
        globalProperties.setKey(KEY);
        globalProperties.setPropertyType(savedPropertyType);

        globalPropertiesService.create(globalProperties);
        //then
        GlobalProperty foundGlobalProperties = globalPropertiesRepository.findByKey(globalProperties.getKey());

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

        final GlobalProperty globalProperties = new GlobalProperty();

        globalProperties.setValue(VALUE);
        globalProperties.setKey(KEY);
        globalProperties.setPropertyType(propertyType);

        final GlobalProperty savedGlobalProperty = globalPropertiesRepository.save(globalProperties);

        savedGlobalProperty.setValue(NEW_VALUE);
        propertyType.setName(NEW_NAME);

        savedGlobalProperty.setPropertyType(propertyType);

        globalPropertiesService.update(savedGlobalProperty);
        //then
        GlobalProperty foundGlobalProperties = globalPropertiesRepository.findByKey(savedGlobalProperty.getKey());

        assertEquals(savedGlobalProperty, foundGlobalProperties);
        assertEquals(savedGlobalProperty.getKey(), foundGlobalProperties.getKey());
        assertEquals(savedGlobalProperty.getValue(), foundGlobalProperties.getValue());
        assertEquals(savedGlobalProperty.getPropertyType(), foundGlobalProperties.getPropertyType());

    }

    @Test
    public void getByKeySuccess() {
        //when
        final PropertyType propertyType = new PropertyType();

        propertyType.setName(NAME);
        propertyType.setSchema(SCHEMA);

        GlobalProperty globalProperties = new GlobalProperty();

        globalProperties.setValue(VALUE);
        globalProperties.setKey(KEY);
        globalProperties.setPropertyType(propertyType);

        globalPropertiesRepository.save(globalProperties);

        IGlobalProperty foundGlobalProperties = globalPropertiesService.getByKey(globalProperties.getKey());
        //then

        assertEquals(globalProperties, foundGlobalProperties);
        assertEquals(globalProperties.getKey(), foundGlobalProperties.getKey());
        assertEquals(globalProperties.getValue(), foundGlobalProperties.getValue());
        assertEquals(globalProperties.getPropertyType(), foundGlobalProperties.getPropertyType());
    }

    @Test
    public void getAllSuccess() {
        //when
        final PropertyType propertyType = new PropertyType();
        final PropertyType propertyType1 = new PropertyType();

        propertyType.setName(NAME);
        propertyType.setSchema(SCHEMA);

        propertyType1.setName(NAME + "1");
        propertyType1.setSchema(SCHEMA + "1");

        GlobalProperty globalProperties = new GlobalProperty();
        GlobalProperty globalProperties1 = new GlobalProperty();

        globalProperties.setValue(VALUE);
        globalProperties.setKey(KEY);
        globalProperties.setPropertyType(propertyType);

        globalProperties1.setValue(VALUE + "1");
        globalProperties1.setKey(KEY + "1");
        globalProperties1.setPropertyType(propertyType1);

        globalPropertiesRepository.save(globalProperties);
        globalPropertiesRepository.save(globalProperties1);

        List<IGlobalProperty> foundGlobalPropertiesList = globalPropertiesService.getAll();
        //then

        assertEquals(foundGlobalPropertiesList.size(), 2);
        assertEquals(foundGlobalPropertiesList.get(0), globalProperties);
        assertEquals(foundGlobalPropertiesList.get(1), globalProperties1);
    }

    @Test
    public void deleteByKey() {
        //when
        final PropertyType propertyType = new PropertyType();

        propertyType.setName(NAME);
        propertyType.setSchema(SCHEMA);

        GlobalProperty globalProperties = new GlobalProperty();

        globalProperties.setValue(VALUE);
        globalProperties.setKey(KEY);
        globalProperties.setPropertyType(propertyType);

        globalPropertiesRepository.save(globalProperties);

        globalPropertiesService.deleteByKey(globalProperties.getKey());
        //then
        GlobalProperty foundGlobalProperties = globalPropertiesRepository.findByKey(globalProperties.getKey());

        assertNull(foundGlobalProperties);
    }
}