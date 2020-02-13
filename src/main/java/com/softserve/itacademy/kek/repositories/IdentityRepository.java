package com.softserve.itacademy.kek.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softserve.itacademy.kek.models.Identity;

public interface IdentityRepository extends CrudRepository<Identity, Long> {
}
