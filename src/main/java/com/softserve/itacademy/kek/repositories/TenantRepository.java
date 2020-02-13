package com.softserve.itacademy.kek.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softserve.itacademy.kek.models.Tenant;

public interface TenantRepository extends CrudRepository<Tenant, Long> {
}
