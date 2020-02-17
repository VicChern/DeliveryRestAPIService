package com.softserve.itacademy.kek.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Tenant findByGuid(UUID guid);

    void removeByGuid(UUID guid);
}
