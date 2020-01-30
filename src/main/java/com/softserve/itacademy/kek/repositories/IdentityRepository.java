package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.Identity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityRepository extends CrudRepository<Identity, Long> {
}
