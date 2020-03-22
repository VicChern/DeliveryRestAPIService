package com.softserve.itacademy.kek.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.Identity;

public interface IdentityRepository extends JpaRepository<Identity, Long> {

    /**
     * Find identity by user and type of identity
     *
     * @param userGuid user guid
     * @param typeName identity type name
     * @return the identity or {@literal Optional#empty()} if none found
     */
    Optional<Identity> findByUserGuidAndIdentityTypeName(UUID userGuid, String typeName);
}
