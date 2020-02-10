package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.TenantDetails;

import java.util.List;
import java.util.Optional;

public interface ITenantDetailsService {
    Optional<TenantDetails> save(TenantDetails tenantDetails);

    Optional<Iterable<TenantDetails>> saveAll(List<TenantDetails> tenantDetailsList);

    Optional<TenantDetails> update(TenantDetails tenantDetails);

    Optional<TenantDetails> get(Long id);

    List<TenantDetails> getAll();

    void deleteById(Long id);

    void deleteAll();
}
