package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.UserDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends CrudRepository<UserDetails, Integer> {
}
