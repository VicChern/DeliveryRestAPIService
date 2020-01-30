package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
}
