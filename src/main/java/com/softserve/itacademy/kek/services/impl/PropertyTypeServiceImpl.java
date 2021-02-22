package com.softserve.itacademy.kek.services.impl;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional(readOnly = true)
    @Override
    public IPropertyType getByName(String name) throws PropertyTypeServiceException {
        logger.info("Get property type from DB by name: {}", name);

        try {
            final PropertyType propertyType = propertyTypeRepository.getByName(name).orElseThrow(
                    () -> new NoSuchElementException("Property type was not found"));

            logger.debug("Property type was gotten from DB: {}", propertyType);

            return propertyType;
        } catch (Exception ex) {
            logger.error("Error while getting property type from DB: " + name, ex);
            throw new PropertyTypeServiceException("An error occurred while getting property type", ex);
        }
    }
}
