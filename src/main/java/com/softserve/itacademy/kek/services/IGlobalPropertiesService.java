package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.GlobalProperties;

import java.util.List;
import java.util.Optional;

public interface IGlobalPropertiesService {
    Optional<GlobalProperties> save(GlobalProperties globalProperties);

    Optional<Iterable<GlobalProperties>> saveAll(List<GlobalProperties> globalPropertiesList);

    Optional<GlobalProperties> update(GlobalProperties globalProperties);

    Optional<GlobalProperties> get(Long id);

    List<GlobalProperties> getAll();

    void deleteById(Long id);

    void deleteAll();
}
