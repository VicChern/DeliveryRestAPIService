package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.Address;

import java.util.List;
import java.util.Optional;

public interface IAddressService {

    Optional<Address> save(Address address);

    Optional<Iterable<Address>> saveAll(List<Address> addresses);

    Optional<Address> update(Address address);

    Optional<Address> get(Long id);

    List<Address> getAll();

    void deleteById(Long id);

    void deleteAll();
}
