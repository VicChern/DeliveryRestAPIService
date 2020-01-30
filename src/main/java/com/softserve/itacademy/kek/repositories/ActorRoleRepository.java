package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.ActorRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRoleRepository extends CrudRepository<ActorRole, Long> {
}
