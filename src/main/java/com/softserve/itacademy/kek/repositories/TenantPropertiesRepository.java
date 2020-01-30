package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.TenantProperties;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantPropertiesRepository extends CrudRepository<TenantProperties, Long> {
}
