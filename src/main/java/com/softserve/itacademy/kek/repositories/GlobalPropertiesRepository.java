package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.GlobalProperties;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalPropertiesRepository extends CrudRepository<GlobalProperties, Long> {
}
