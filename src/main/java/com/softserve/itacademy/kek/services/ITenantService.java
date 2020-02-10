package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.Tenant;

import java.util.List;
import java.util.Optional;

public interface ITenantService {
    Optional<Tenant> save(Tenant tenant);

    Optional<Iterable<Tenant>> saveAll(List<Tenant> tenants);

    Optional<Tenant> update(Tenant tenant);

    Optional<Tenant> get(Long id);

    List<Tenant> getAll();

    void deleteById(Long id);

    void deleteAll();
}
