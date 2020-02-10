package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.models.TenantProperties;
import com.softserve.itacademy.kek.repositories.TenantPropertiesRepository;
import com.softserve.itacademy.kek.services.ITenantPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantPropertiesServiceImpl implements ITenantPropertiesService {

    private TenantPropertiesRepository tenantPropertiesRepository;

    @Autowired
    public TenantPropertiesServiceImpl(TenantPropertiesRepository tenantPropertiesRepository) {
        this.tenantPropertiesRepository = tenantPropertiesRepository;
    }

    @Override
    public Optional<TenantProperties> save(TenantProperties tenantProperties) {
        return Optional.of(tenantPropertiesRepository.save(tenantProperties));
    }

    @Override
    public Optional<Iterable<TenantProperties>> saveAll(List<TenantProperties> tenantPropertiesList) {
        return Optional.of(tenantPropertiesRepository.saveAll(tenantPropertiesList));
    }

    @Override
    public Optional<TenantProperties> update(TenantProperties tenantProperties) {
        return Optional.of(tenantPropertiesRepository.save(tenantProperties));
    }

    @Override
    public Optional<TenantProperties> get(Long id) {
        return tenantPropertiesRepository.findById(id);
    }

    @Override
    public List<TenantProperties> getAll() {
        return (List<TenantProperties>) tenantPropertiesRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        tenantPropertiesRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        tenantPropertiesRepository.deleteAll();
    }

}
