package com.softserve.itacademy.kek.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.Address;

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
    Address findByGuid(UUID guid);

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
