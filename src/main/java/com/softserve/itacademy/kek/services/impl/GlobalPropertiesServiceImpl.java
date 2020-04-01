package com.softserve.itacademy.kek.services.impl;

import javax.validation.ConstraintViolationException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    private final static Logger logger = LoggerFactory.getLogger(IGlobalPropertiesService.class);

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
    public IGlobalProperty create(IGlobalProperty globalProperty) throws GlobalPropertiesServiceException {
        logger.info("Insert Global Property into DB: {}", globalProperty);

        final GlobalProperty actualProperties = new GlobalProperty();
        final PropertyType actualPropertyType = (PropertyType) propertyTypeService.produce(globalProperty.getPropertyType());

        actualProperties.setPropertyType(actualPropertyType);
        actualProperties.setKey(globalProperty.getKey());
        actualProperties.setValue(globalProperty.getValue());

        try {
            final GlobalProperty insertedGlobalProperty = globalPropertiesRepository.saveAndFlush(actualProperties);

            logger.debug("Global Property was inserted into DB: {}", insertedGlobalProperty);

            return insertedGlobalProperty;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while inserting Global Property into DB: " + globalProperty, ex);
            throw new GlobalPropertiesServiceException("An error occurs while inserting global property", ex);
        }
    }

    @Transactional
    @Override
    public IGlobalProperty update(IGlobalProperty globalProperty) throws GlobalPropertiesServiceException {
        logger.info("Update Global Property in DB: {}", globalProperty);

        final GlobalProperty actualProperties = globalPropertiesRepository.findByIdProperty(globalProperty.getIdProperty());
        final PropertyType actualPropertyType = (PropertyType) propertyTypeService.produce(globalProperty.getPropertyType());

        actualProperties.setPropertyType(actualPropertyType);
        actualProperties.setKey(globalProperty.getKey());
        actualProperties.setValue(globalProperty.getValue());

        try {
            final GlobalProperty updatedGlobalProperty = globalPropertiesRepository.saveAndFlush(actualProperties);

            logger.debug("Global Property was updated into DB: {}", updatedGlobalProperty);

            return updatedGlobalProperty;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while updating Global Property in DB", ex);
            throw new GlobalPropertiesServiceException("An error occurs while updating global property", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IGlobalProperty getByKey(String key) throws GlobalPropertiesServiceException {
        logger.info("Get Global Property from DB by key: {}", key);

        try {
            return globalPropertiesRepository.findByKey(key);
        } catch (DataAccessException ex) {
            logger.error("Error while getting Global Properties by key: " + key, ex);
            throw new GlobalPropertiesServiceException("An error occurred while getting global properties", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<IGlobalProperty> getAll() throws GlobalPropertiesServiceException {
        logger.info("Get all Global Property");

        try {
            final List<? extends IGlobalProperty> globalPropertiesList = globalPropertiesRepository.findAll();
            return (List<IGlobalProperty>) globalPropertiesList;
        } catch (DataAccessException ex) {
            logger.error("Error while getting all Global Properties", ex);
            throw new GlobalPropertiesServiceException("An error occurred while getting global properties", ex);
        }
    }

    @Transactional
    @Override
    public void deleteByKey(String key) {
        logger.info("Delete Global Property by key: {}", key);

        try {
            globalPropertiesRepository.deleteByKey(key);
            globalPropertiesRepository.flush();

            logger.debug("Global Property was deleted from DB: key = {}", key);
        } catch (DataAccessException ex) {
            logger.error("Error while deleting Global Property by key: " + key, ex);
            throw new GlobalPropertiesServiceException("An error occurred while deleting global property", ex);
        }
    }
}
