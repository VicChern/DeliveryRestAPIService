package com.softserve.itacademy.kek.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.models.IGlobalProperties;
import com.softserve.itacademy.kek.models.impl.GlobalProperties;
import com.softserve.itacademy.kek.models.impl.PropertyType;
import com.softserve.itacademy.kek.repositories.GlobalPropertiesRepository;
import com.softserve.itacademy.kek.repositories.PropertyTypeRepository;
import com.softserve.itacademy.kek.services.IGlobalPropertiesService;
import com.softserve.itacademy.kek.services.IOrderService;

@Service
public class GlobalPropertiesServiceImpl implements IGlobalPropertiesService {

    private final static Logger LOGGER = LoggerFactory.getLogger(IOrderService.class);

    private final PropertyTypeRepository propertyTypeRepository;
    private final GlobalPropertiesRepository globalPropertiesRepository;

    @Autowired
    public GlobalPropertiesServiceImpl(GlobalPropertiesRepository globalPropertiesRepository, PropertyTypeRepository propertyTypeRepository) {
        this.propertyTypeRepository = propertyTypeRepository;
        this.globalPropertiesRepository = globalPropertiesRepository;
    }

    @Transactional
    @Override
    public IGlobalProperties create(IGlobalProperties globalProperties) throws Exception {
        LOGGER.info("Saving globalProperties: {}", globalProperties);

        final PropertyType actualPropertyType;
        final PropertyType foundPropertyType = propertyTypeRepository.getByName(globalProperties.getPropertyType().getName());
        final GlobalProperties actualProperties = new GlobalProperties();

        if (foundPropertyType == null) {
            actualPropertyType = propertyTypeRepository.save((PropertyType) globalProperties.getPropertyType());
        } else {
            actualPropertyType = foundPropertyType;
        }

        actualProperties.setPropertyType(actualPropertyType);
        actualProperties.setKey(globalProperties.getKey());
        actualProperties.setValue(globalProperties.getValue());

        try {
            return globalPropertiesRepository.save(actualProperties);
        } catch (Exception e) {
            LOGGER.error("Can`t save global properties: {}, exception: {}", globalProperties, e.getMessage());
            throw new Exception("Can`t save global properties: " + globalProperties + ", exception: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public IGlobalProperties update(IGlobalProperties globalProperties) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public IGlobalProperties getByKey(String keu) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<IGlobalProperties> getAll() {
        return null;
    }

    @Override
    public void deleteByKey(String key) {

    }
}
