package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.Identity;
import org.springframework.data.repository.CrudRepository;

public interface IdentityRepository extends CrudRepository<Identity, Long> {
}
