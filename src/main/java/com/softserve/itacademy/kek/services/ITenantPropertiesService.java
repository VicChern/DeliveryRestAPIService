package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.TenantProperties;

import java.util.List;
import java.util.Optional;

public interface ITenantPropertiesService {
    Optional<TenantProperties> save(TenantProperties tenantProperties);

    Optional<Iterable<TenantProperties>> saveAll(List<TenantProperties> tenantPropertiesList);

    Optional<TenantProperties> update(TenantProperties tenantProperties);

    Optional<TenantProperties> get(Long id);

    List<TenantProperties> getAll();

    void deleteById(Long id);

    void deleteAll();
}
