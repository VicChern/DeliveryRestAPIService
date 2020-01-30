package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.Actor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends CrudRepository<Actor, Long> {
}
