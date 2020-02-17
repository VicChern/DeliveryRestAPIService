package com.softserve.itacademy.kek.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.TenantProperties;

public interface TenantPropertiesRepository extends JpaRepository<TenantProperties, Long> {

    TenantProperties findByGuid(UUID guid);

    TenantProperties findByGuidAndTenantGuid(UUID guid, UUID tenantGuid);

    boolean existsTenantPropertiesByGuidAndTenantGuid(UUID guid, UUID tenantGuid);

    void removeByGuidAndTenantGuid(UUID guid, UUID tenantGuid);

}
