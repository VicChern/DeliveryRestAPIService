package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.services.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantServiceImpl implements ITenantService {

    private TenantRepository tenantRepository;

    @Autowired
    public TenantServiceImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Optional<Tenant> save(Tenant tenant) {
        return Optional.of(tenantRepository.save(tenant));
    }

    @Override
    public Optional<Iterable<Tenant>> saveAll(List<Tenant> tenantList) {
        return Optional.of(tenantRepository.saveAll(tenantList));
    }

    @Override
    public Optional<Tenant> update(Tenant tenant) {
        return Optional.of(tenantRepository.save(tenant));
    }

    @Override
    public Optional<Tenant> get(Long id) {
        return tenantRepository.findById(id);
    }

    @Override
    public List<Tenant> getAll() {
        return (List<Tenant>) tenantRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        tenantRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        tenantRepository.deleteAll();
    }

}
