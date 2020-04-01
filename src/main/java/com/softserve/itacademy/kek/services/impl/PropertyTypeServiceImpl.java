package com.softserve.itacademy.kek.services.impl;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.PropertyTypeServiceException;
import com.softserve.itacademy.kek.models.IPropertyType;
import com.softserve.itacademy.kek.models.impl.PropertyType;
import com.softserve.itacademy.kek.repositories.PropertyTypeRepository;
import com.softserve.itacademy.kek.services.IPropertyTypeService;

@Service
public class PropertyTypeServiceImpl implements IPropertyTypeService {
    private final static Logger logger = LoggerFactory.getLogger(PropertyTypeServiceImpl.class);

    private final PropertyTypeRepository propertyTypeRepository;

    @Autowired
    public PropertyTypeServiceImpl(PropertyTypeRepository propertyTypeRepository) {
        this.propertyTypeRepository = propertyTypeRepository;
    }

    @Transactional
    @Override
    public IPropertyType create(IPropertyType propertyType) throws PropertyTypeServiceException {
        logger.info("Insert Property Type into DB: {}", propertyType);

        final PropertyType actualPropertyType = new PropertyType();
        actualPropertyType.setName(propertyType.getName());
        actualPropertyType.setSchema(propertyType.getSchema());

        try {
            final PropertyType insertedPropertyType = propertyTypeRepository.saveAndFlush(actualPropertyType);

            logger.debug("Property Type was inserted into DB: {}", insertedPropertyType);

            return insertedPropertyType;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while inserting Property Type into DB: " + actualPropertyType, ex);
            throw new PropertyTypeServiceException("An error occurred while inserting property type", ex);
        }
    }

    @Transactional
    @Override
    public IPropertyType update(IPropertyType propertyType) throws PropertyTypeServiceException {
        logger.info("Update Property Type in DB: {}", propertyType);

        final PropertyType actualPropertyType = (PropertyType) getByName(propertyType.getName());
        actualPropertyType.setName(propertyType.getName());
        actualPropertyType.setSchema(propertyType.getSchema());

        try {
            final PropertyType updatedPropertyType = propertyTypeRepository.saveAndFlush(actualPropertyType);

            logger.debug("Property Type was updated in DB: {}", updatedPropertyType);

            return updatedPropertyType;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while updating Property Type in DB: " + propertyType, ex);
            throw new PropertyTypeServiceException("An error occurred while updating property type", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IPropertyType getByName(String name) throws PropertyTypeServiceException {
        logger.info("Get Property Type from DB by name: {}", name);

        final Optional<PropertyType> propertyType = internalGetByName(name);

        return propertyType.orElseThrow(
                () -> {
                    Exception ex = new NoSuchElementException();
                    logger.error("Property Type was not found in DB: " + name, ex);
                    return new PropertyTypeServiceException("Property Type was not found", ex);
                });
    }

    @Transactional
    @Override
    public IPropertyType produce(IPropertyType propertyType) throws PropertyTypeServiceException {
        logger.info("Produce Property Type in DB: {}", propertyType);

        final Optional<PropertyType> actualPropertyType = internalGetByName(propertyType.getName());

        if (actualPropertyType.isEmpty()) {
            return create(propertyType);
        } else {
            return update(propertyType);
        }
    }

    private Optional<PropertyType> internalGetByName(String name) throws PropertyTypeServiceException {
        logger.debug("Get Property Type (Optional object) from DB by name: {}", name);

        final Optional<PropertyType> propertyType;
        try {
            propertyType = propertyTypeRepository.getByName(name);

            logger.debug("Property Type was gotten from DB: {}", propertyType);

            return propertyType;
        } catch (DataAccessException ex) {
            logger.error("Error while getting Property Type from DB: " + name, ex);
            throw new PropertyTypeServiceException("An error occurred while getting property type", ex);
        }
    }
}
