package com.softserve.itacademy.kek.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.softserve.itacademy.kek.models.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {

    Address findByGuid(UUID guid);

}
