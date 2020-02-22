package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.impl.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActorRepository extends JpaRepository<Actor, Long> {

    Actor findByGuid(UUID guid);
}
