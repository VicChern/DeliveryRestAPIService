package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Tenant findByGuid(UUID guid);

    void removeByGuid(UUID guid);
}
