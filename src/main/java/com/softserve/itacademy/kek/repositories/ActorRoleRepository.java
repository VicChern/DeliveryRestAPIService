package com.softserve.itacademy.kek.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.ActorRole;

public interface ActorRoleRepository extends JpaRepository<ActorRole, Long> {

    ActorRole findByName(String name);
}
