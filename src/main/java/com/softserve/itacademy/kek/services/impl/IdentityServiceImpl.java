package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.models.Identity;
import com.softserve.itacademy.kek.repositories.IdentityRepository;
import com.softserve.itacademy.kek.services.IIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IdentityServiceImpl implements IIdentityService {

    private IdentityRepository identityRepository;

    @Autowired
    public IdentityServiceImpl(IdentityRepository identityRepository) {
        this.identityRepository = identityRepository;
    }

    @Override
    public Optional<Identity> save(Identity identity) {
        return Optional.of(identityRepository.save(identity));
    }

    @Override
    public Optional<Iterable<Identity>> saveAll(List<Identity> identities) {
        return Optional.of(identityRepository.saveAll(identities));
    }

    @Override
    public Optional<Identity> update(Identity identity) {
        return Optional.of(identityRepository.save(identity));
    }

    @Override
    public Optional<Identity> get(Long id) {
        return identityRepository.findById(id);
    }

    @Override
    public List<Identity> getAll() {
        return (List<Identity>) identityRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        identityRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        identityRepository.deleteAll();
    }

}
