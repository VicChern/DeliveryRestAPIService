package com.softserve.itacademy.kek.services;

import java.util.List;

import com.softserve.itacademy.kek.models.IGlobalProperties;

public interface IGlobalPropertiesService {

    IGlobalProperties create(IGlobalProperties globalProperties) throws Exception;

    IGlobalProperties update(IGlobalProperties globalProperties);

    IGlobalProperties getByKey(String keu);

    List<IGlobalProperties> getAll();

    void deleteByKey(String key);
}
