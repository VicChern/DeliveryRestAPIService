package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.Tenant;
import org.springframework.data.repository.CrudRepository;

public interface TenantRepository extends CrudRepository<Tenant, Long> {
}
