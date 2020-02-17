package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.TenantProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantPropertiesRepository extends JpaRepository<TenantProperties, Long> {

    TenantProperties findByGuid(UUID guid);

    TenantProperties findByGuidAndTenantGuid(UUID guid, UUID tenantGuid);

    boolean existsTenantPropertiesByGuidAndTenantGuid(UUID guid, UUID tenantGuid);

    void removeByGuidAndTenantGuid(UUID guid, UUID tenantGuid);

}
