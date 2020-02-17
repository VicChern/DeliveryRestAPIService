package com.softserve.itacademy.kek.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByGuid(UUID guid);

}
