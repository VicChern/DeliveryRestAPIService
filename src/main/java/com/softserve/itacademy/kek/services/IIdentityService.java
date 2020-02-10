package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.Identity;

import java.util.List;
import java.util.Optional;

public interface IIdentityService {
    Optional<Identity> save(Identity identity);

    Optional<Iterable<Identity>> saveAll(List<Identity> identities);

    Optional<Identity> update(Identity identity);

    Optional<Identity> get(Long id);

    List<Identity> getAll();

    void deleteById(Long id);

    void deleteAll();
}
