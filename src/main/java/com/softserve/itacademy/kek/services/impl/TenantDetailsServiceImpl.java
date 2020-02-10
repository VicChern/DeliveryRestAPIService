package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.models.TenantDetails;
import com.softserve.itacademy.kek.repositories.TenantDetailsRepository;
import com.softserve.itacademy.kek.services.ITenantDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantDetailsServiceImpl implements ITenantDetailsService {

    private TenantDetailsRepository tenantDetailsRepository;

    @Autowired
    public TenantDetailsServiceImpl(TenantDetailsRepository tenantDetailsRepository) {
        this.tenantDetailsRepository = tenantDetailsRepository;
    }

    @Override
    public Optional<TenantDetails> save(TenantDetails tenantDetails) {
        return Optional.of(tenantDetailsRepository.save(tenantDetails));
    }

    @Override
    public Optional<Iterable<TenantDetails>> saveAll(List<TenantDetails> tenantDetailsList) {
        return Optional.of(tenantDetailsRepository.saveAll(tenantDetailsList));
    }

    @Override
    public Optional<TenantDetails> update(TenantDetails tenantDetails) {
        return Optional.of(tenantDetailsRepository.save(tenantDetails));
    }

    @Override
    public Optional<TenantDetails> get(Long id) {
        return tenantDetailsRepository.findById(id);
    }

    @Override
    public List<TenantDetails> getAll() {
        return (List<TenantDetails>) tenantDetailsRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        tenantDetailsRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        tenantDetailsRepository.deleteAll();
    }

}
