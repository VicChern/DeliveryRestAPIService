package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.models.GlobalProperties;
import com.softserve.itacademy.kek.repositories.GlobalPropertiesRepository;
import com.softserve.itacademy.kek.services.IGlobalPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GlobalPropertiesServiceImpl implements IGlobalPropertiesService {

    private GlobalPropertiesRepository globalPropertiesRepository;

    @Autowired
    public GlobalPropertiesServiceImpl(GlobalPropertiesRepository globalPropertiesRepository) {
        this.globalPropertiesRepository = globalPropertiesRepository;
    }

    @Override
    public Optional<GlobalProperties> save(GlobalProperties globalProperties) {
        return Optional.of(globalPropertiesRepository.save(globalProperties));
    }

    @Override
    public Optional<Iterable<GlobalProperties>> saveAll(List<GlobalProperties> globalPropertiesList) {
        return Optional.of(globalPropertiesRepository.saveAll(globalPropertiesList));
    }

    @Override
    public Optional<GlobalProperties> update(GlobalProperties globalProperties) {
        return Optional.of(globalPropertiesRepository.save(globalProperties));
    }

    @Override
    public Optional<GlobalProperties> get(Long id) {
        return globalPropertiesRepository.findById(id);
    }

    @Override
    public List<GlobalProperties> getAll() {
        return (List<GlobalProperties>) globalPropertiesRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        globalPropertiesRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        globalPropertiesRepository.deleteAll();
    }

}
