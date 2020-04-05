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
        logger.info("Insert property type into DB: propertyType = {}", propertyType);

        final PropertyType actualPropertyType = new PropertyType();
        actualPropertyType.setName(propertyType.getName());
        actualPropertyType.setSchema(propertyType.getSchema());

        try {
            final PropertyType insertedPropertyType = propertyTypeRepository.saveAndFlush(actualPropertyType);

            logger.debug("Property type was inserted into DB: {}", insertedPropertyType);

            return insertedPropertyType;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while inserting property type into DB: " + actualPropertyType, ex);
            throw new PropertyTypeServiceException("An error occurred while inserting property type", ex);
        }
    }

    @Transactional
    @Override
    public IPropertyType update(IPropertyType propertyType) throws PropertyTypeServiceException {
        logger.info("Update property type in DB: propertyType = {}", propertyType);

        final PropertyType actualPropertyType = (PropertyType) getByName(propertyType.getName());
        actualPropertyType.setName(propertyType.getName());
        actualPropertyType.setSchema(propertyType.getSchema());

        try {
            final PropertyType updatedPropertyType = propertyTypeRepository.saveAndFlush(actualPropertyType);

            logger.debug("Property type was updated in DB: {}", updatedPropertyType);

            return updatedPropertyType;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while updating property type in DB: " + propertyType, ex);
            throw new PropertyTypeServiceException("An error occurred while updating property type", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IPropertyType getByName(String name) throws PropertyTypeServiceException {
        logger.info("Get property type from DB: name = {}", name);

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
        logger.info("Produce property type in DB: {}", propertyType);

        final Optional<PropertyType> actualPropertyType = internalGetByName(propertyType.getName());

        if (actualPropertyType.isEmpty()) {
            return create(propertyType);
        } else {
            return update(propertyType);
        }
    }

    private Optional<PropertyType> internalGetByName(String name) throws PropertyTypeServiceException {
        logger.debug("Get property type (Optional object) from DB: name = {}", name);

        final Optional<PropertyType> propertyType;
        try {
            propertyType = propertyTypeRepository.getByName(name);

            logger.debug("Property type was read from DB: {}", propertyType);

            return propertyType;
        } catch (DataAccessException ex) {
            logger.error("Error while getting property type from DB: " + name, ex);
            throw new PropertyTypeServiceException("An error occurred while getting property type", ex);
        }
    }
}
