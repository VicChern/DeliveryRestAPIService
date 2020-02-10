package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.UserDetails;
import org.springframework.data.repository.CrudRepository;

public interface UserDetailsRepository extends CrudRepository<UserDetails, Long> {
}
