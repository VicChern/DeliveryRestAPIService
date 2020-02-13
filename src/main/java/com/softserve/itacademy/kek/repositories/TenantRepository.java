package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.Tenant;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TenantRepository extends CrudRepository<Tenant, Long> {

    Tenant findByGuid(UUID guid);

    void removeByGuid(UUID guid);
}
