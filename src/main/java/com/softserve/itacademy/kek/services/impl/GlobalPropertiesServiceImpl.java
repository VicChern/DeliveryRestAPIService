package com.softserve.itacademy.kek.services.impl;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;

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
        logger.info("Insert global property into DB: key = {}", globalProperty.getKey());

        final GlobalProperty actualProperties = new GlobalProperty();
        final PropertyType actualPropertyType = (PropertyType) propertyTypeService.produce(globalProperty.getPropertyType());

        actualProperties.setPropertyType(actualPropertyType);
        actualProperties.setKey(globalProperty.getKey());
        actualProperties.setValue(globalProperty.getValue());

        try {
            final GlobalProperty insertedGlobalProperty = globalPropertiesRepository.saveAndFlush(actualProperties);

            logger.debug("Global property was inserted into DB: {}", insertedGlobalProperty);

            return insertedGlobalProperty;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while inserting global property into DB: " + globalProperty, ex);
            throw new GlobalPropertiesServiceException("An error occurs while inserting global property", ex);
        }
    }

    @Transactional
    @Override
    public IGlobalProperty update(IGlobalProperty globalProperty) throws GlobalPropertiesServiceException {
        logger.info("Update global property in DB: key = {}", globalProperty.getKey());

        final GlobalProperty actualProperties = globalPropertiesRepository.findByIdProperty(globalProperty.getIdProperty());
        final PropertyType actualPropertyType = (PropertyType) propertyTypeService.produce(globalProperty.getPropertyType());

        actualProperties.setPropertyType(actualPropertyType);
        actualProperties.setKey(globalProperty.getKey());
        actualProperties.setValue(globalProperty.getValue());

        try {
            final GlobalProperty updatedGlobalProperty = globalPropertiesRepository.saveAndFlush(actualProperties);

            logger.debug("Global property was updated in DB: {}", updatedGlobalProperty);

            return updatedGlobalProperty;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while updating global property in DB: " + actualProperties, ex);
            throw new GlobalPropertiesServiceException("An error occurs while updating global property", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IGlobalProperty getByKey(String key) throws GlobalPropertiesServiceException {
        logger.info("Get global property from DB: key = {}", key);

        try {
            final GlobalProperty globalProperty = globalPropertiesRepository.findByKey(key).orElseThrow(
                    () -> {
                        logger.error("Global property wasn't found in the database");
                        return new GlobalPropertiesServiceException("User was not found in database for guid: " + key, new NoSuchElementException());
                    });

            logger.debug("Global property was gotten from DB: globalProperty = {}", globalProperty);

            return globalProperty;

        } catch (DataAccessException ex) {
            logger.error("Error while getting global property: key = " + key, ex);
            throw new GlobalPropertiesServiceException("An error occurred while getting global property", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<IGlobalProperty> getAll() throws GlobalPropertiesServiceException {
        logger.info("Get all global properties");

        try {
            final List<? extends IGlobalProperty> globalPropertiesList = globalPropertiesRepository.findAll();

            logger.debug("Global properties was read from DB");

            return (List<IGlobalProperty>) globalPropertiesList;
        } catch (DataAccessException ex) {
            logger.error("Error while getting all global properties", ex);
            throw new GlobalPropertiesServiceException("An error occurred while getting global properties", ex);
        }
    }

    @Transactional
    @Override
    public void deleteByKey(String key) throws GlobalPropertiesServiceException {
        logger.info("Delete global property from DB: key = {}", key);

        final GlobalProperty globalProperty = (GlobalProperty) getByKey(key);

        try {
            globalPropertiesRepository.deleteById(globalProperty.getIdProperty());
            globalPropertiesRepository.flush();

            logger.debug("Global property was deleted from DB: key = {}", key);
        } catch (DataAccessException ex) {
            logger.error("Error while deleting global property: key = " + key, ex);
            throw new GlobalPropertiesServiceException("An error occurred while deleting global property", ex);
        }
    }
}
