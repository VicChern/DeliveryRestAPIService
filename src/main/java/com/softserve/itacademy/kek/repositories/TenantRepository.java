package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.impl.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByGuid(UUID guid);

}
