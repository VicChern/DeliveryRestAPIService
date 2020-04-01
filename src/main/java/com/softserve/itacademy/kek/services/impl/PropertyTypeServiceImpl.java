package com.softserve.itacademy.kek.services.impl;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.exception.PropertyTypeServiceException;
import com.softserve.itacademy.kek.models.IPropertyType;
import com.softserve.itacademy.kek.models.impl.PropertyType;
import com.softserve.itacademy.kek.repositories.PropertyTypeRepository;
import com.softserve.itacademy.kek.services.IPropertyTypeService;

@Service
public class PropertyTypeServiceImpl implements IPropertyTypeService {
    private final static Logger LOGGER = LoggerFactory.getLogger(PropertyTypeServiceImpl.class);

    private final PropertyTypeRepository propertyTypeRepository;

    @Autowired
    public PropertyTypeServiceImpl(PropertyTypeRepository propertyTypeRepository) {
        this.propertyTypeRepository = propertyTypeRepository;
    }

    @Transactional
    public IPropertyType createOrUpdate(IPropertyType typeData) throws PropertyTypeServiceException {
        LOGGER.info("Getting Property Type: {}", typeData);

        final PropertyType dbPropertyType;
        try {
            dbPropertyType = propertyTypeRepository.findByName(typeData.getName()).orElseThrow(() -> {
                LOGGER.debug("Get PropertyType  by name from db: {}", typeData);
                return new PropertyTypeServiceException("Property type was not found in database for type of data: " + typeData, new NoSuchElementException());
            });
            return dbPropertyType;
        } catch (DataAccessException ex) {
            LOGGER.error("An error occurred while getting Property Type " + typeData, ex);
            throw new PropertyTypeServiceException("An error occurred while getting property type", ex);
        }
    }

    private IPropertyType internalCreate(IPropertyType typeData) throws PropertyTypeServiceException {
        LOGGER.debug("Inserting Property Type into DB: {}", typeData);

        final PropertyType propertyType = new PropertyType();
        propertyType.setName(typeData.getName());
        propertyType.setSchema(typeData.getSchema());

        try {
            final PropertyType dbPropertyType = propertyTypeRepository.saveAndFlush(propertyType);

            LOGGER.debug("Property Type was inserted into DB: {}", dbPropertyType);

            return dbPropertyType;
        } catch (ConstraintViolationException | DataAccessException ex) {
            LOGGER.error("An error occurred while inserting Property Type " + typeData, ex);
            throw new PropertyTypeServiceException("An error occurred while inserting property type", ex);
        }
    }

    private IPropertyType internalUpdate(PropertyType propertyType, IPropertyType typeData) throws PropertyTypeServiceException {
        LOGGER.debug("Updating Property Type in DB: {}", typeData);

        if (typeData.getName() != null) {
            propertyType.setName(typeData.getName());
        }
        if (typeData.getSchema() != null) {
            propertyType.setSchema(typeData.getSchema());
        }

        try {
            final PropertyType dbPropertyType = propertyTypeRepository.saveAndFlush(propertyType);

            LOGGER.debug("Property Type was updated into DB: {}", dbPropertyType);

            return dbPropertyType;
        } catch (ConstraintViolationException | DataAccessException ex) {
            LOGGER.error("An error occurred while updating Property Type " + propertyType, ex);
            throw new PropertyTypeServiceException("An error occurred while updating property type", ex);
        }
    }
}
