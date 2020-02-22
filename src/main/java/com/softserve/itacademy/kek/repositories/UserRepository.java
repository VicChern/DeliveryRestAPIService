package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.impl.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByGuid(UUID guid);

}
