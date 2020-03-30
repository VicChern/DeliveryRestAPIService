package com.softserve.itacademy.kek.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.GlobalPropertiesServiceException;
import com.softserve.itacademy.kek.models.IGlobalProperty;
import com.softserve.itacademy.kek.models.impl.GlobalProperty;
import com.softserve.itacademy.kek.models.impl.PropertyType;
import com.softserve.itacademy.kek.repositories.GlobalPropertiesRepository;
import com.softserve.itacademy.kek.services.IGlobalPropertiesService;
import com.softserve.itacademy.kek.services.IPropertyTypeService;

@Service
public class GlobalPropertiesServiceImpl implements IGlobalPropertiesService {

    private final static Logger LOGGER = LoggerFactory.getLogger(IGlobalPropertiesService.class);

    private final GlobalPropertiesRepository globalPropertiesRepository;
    private final IPropertyTypeService propertyTypeService;

    @Autowired
    public GlobalPropertiesServiceImpl(GlobalPropertiesRepository globalPropertiesRepository,
                                       IPropertyTypeService propertyTypeService) {
        this.globalPropertiesRepository = globalPropertiesRepository;
        this.propertyTypeService = propertyTypeService;
    }

    @Transactional
    @Override
    public IGlobalProperty create(IGlobalProperty globalProperties) throws GlobalPropertiesServiceException {
        LOGGER.info("Saving globalProperties: {}", globalProperties);

        final GlobalProperty actualProperties = new GlobalProperty();
        final PropertyType actualPropertyType = (PropertyType) propertyTypeService.createOrUpdate(globalProperties.getPropertyType());

        actualProperties.setPropertyType(actualPropertyType);
        actualProperties.setKey(globalProperties.getKey());
        actualProperties.setValue(globalProperties.getValue());

        try {
            return globalPropertiesRepository.save(actualProperties);
        } catch (Exception e) {
            LOGGER.error("Can`t save global properties: {}, exception: {}", globalProperties, e.getMessage());
            throw new GlobalPropertiesServiceException("Can`t save global properties: " + globalProperties, e);
        }
    }

    @Transactional
    @Override
    public IGlobalProperty update(IGlobalProperty globalProperties) throws GlobalPropertiesServiceException {
        LOGGER.info("Updating globalProperties: {}", globalProperties);

        final GlobalProperty actualProperties = globalPropertiesRepository.findByIdProperty(globalProperties.getIdProperty());
        final PropertyType actualPropertyType = (PropertyType) propertyTypeService.createOrUpdate(globalProperties.getPropertyType());

        actualProperties.setPropertyType(actualPropertyType);
        actualProperties.setKey(globalProperties.getKey());
        actualProperties.setValue(globalProperties.getValue());

        try {
            return globalPropertiesRepository.save(actualProperties);
        } catch (Exception e) {
            LOGGER.error("Can`t update global properties: {}, exception: {}", globalProperties, e.getMessage());
            throw new GlobalPropertiesServiceException("Can`t update global properties: " + globalProperties, e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IGlobalProperty getByKey(String key) throws GlobalPropertiesServiceException {
        LOGGER.info("Getting globalProperties by key: {}", key);

        try {
            return globalPropertiesRepository.findByKey(key);
        } catch (Exception e) {
            LOGGER.error("An error: {}, occurred while getting GlobalProperties by key: {}", e.getMessage(), key);
            throw new GlobalPropertiesServiceException("An error occurred while getting GlobalProperties by key: " + key, e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<IGlobalProperty> getAll() throws GlobalPropertiesServiceException {
        LOGGER.info("Getting all globalProperties");

        List<? extends IGlobalProperty> globalPropertiesList;

        try {
            globalPropertiesList = globalPropertiesRepository.findAll();
        } catch (Exception e) {
            LOGGER.error("An error: {}, occurred while getting all GlobalProperties", e.getMessage());
            throw new GlobalPropertiesServiceException("An error occurred while getting all GlobalProperties: ", e);
        }

        return (List<IGlobalProperty>) globalPropertiesList;
    }

    @Transactional
    @Override
    public void deleteByKey(String key) {
        LOGGER.info("Deleting globalProperties by key: {}", key);

        try {
            globalPropertiesRepository.deleteByKey(key);
        } catch (Exception e) {
            LOGGER.error("An error: {}, occurred while deleting GlobalProperties by key: {}", e.getMessage(), key);
            throw new GlobalPropertiesServiceException("An error occurred while deleting GlobalProperties by key: " + key, e);
        }
    }
}
