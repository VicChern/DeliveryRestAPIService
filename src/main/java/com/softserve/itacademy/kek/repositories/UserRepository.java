package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
