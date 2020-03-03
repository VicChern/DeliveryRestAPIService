package com.softserve.itacademy.kek.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.Actor;

public interface ActorRepository extends JpaRepository<Actor, Long> {

    Actor findByGuid(UUID guid);

    Actor findByUser(IUser user);
}
