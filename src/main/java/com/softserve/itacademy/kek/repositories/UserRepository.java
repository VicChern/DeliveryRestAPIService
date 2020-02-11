package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByGuid(UUID guid);

}
