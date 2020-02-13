package com.softserve.itacademy.kek.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softserve.itacademy.kek.models.UserDetails;

public interface UserDetailsRepository extends CrudRepository<UserDetails, Long> {
}
