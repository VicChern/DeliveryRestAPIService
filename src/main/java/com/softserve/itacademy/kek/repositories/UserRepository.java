package com.softserve.itacademy.kek.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.User;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user by its guid.
     *
     * @param guid
     * @return the user with the given guid or {@literal Optional#empty()} if none found
     */
    Optional<User> findByGuid(UUID guid);

    User findByEmail(String email);

}
