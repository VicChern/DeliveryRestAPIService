package com.vicchern.deliveryservice.repositories;

import com.vicchern.deliveryservice.models.impl.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for work with Address
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Returns address by guid
     *
     * @param guid address guid
     * @return address
     */
    Optional<Address> findByGuid(UUID guid);

    /**
     * Returns tenant address by address guid and tenant guid
     *
     * @param guid       address guid
     * @param tenantGuid tenant guid
     * @return tenant address
     */
    Optional<Address> findByGuidAndTenantGuid(UUID guid, UUID tenantGuid);

    /**
     * Returns user address by address guid and user guid
     *
     * @param guid     address guid
     * @param userGuid user guid
     * @return user address
     */
    Optional<Address> findByGuidAndUserGuid(UUID guid, UUID userGuid);

    /**
     * Returns user address list
     *
     * @param guid user guid
     * @return user address list
     */
    List<Address> findAllByUserGuid(UUID guid);

    /**
     * Returns tenant address list
     *
     * @param guid tenant guid
     * @return tenant address list
     */
    List<Address> findAllByTenantGuid(UUID guid);
}
