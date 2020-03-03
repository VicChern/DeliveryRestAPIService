package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.impl.TenantProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantPropertiesRepository extends JpaRepository<TenantProperties, Long> {

    Optional<TenantProperties> findByGuid(UUID guid);

    Optional<TenantProperties> findByGuidAndTenantGuid(UUID guid, UUID tenantGuid);

}
