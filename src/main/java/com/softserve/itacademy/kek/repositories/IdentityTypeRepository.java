package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.IdentityType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityTypeRepository extends CrudRepository<IdentityType, Long> {
}
