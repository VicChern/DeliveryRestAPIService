package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.impl.ActorRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRoleRepository extends JpaRepository<ActorRole, Long> {

    ActorRole findByName(String name);
}
