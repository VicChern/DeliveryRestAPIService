package com.softserve.itacademy.kek.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.impl.Identity;

public interface IdentityRepository extends JpaRepository<Identity, Long> {

    /**
     * Find identity by user and type of identity
     *
     * @param email    user email
     * @param typeName identity type name
     * @return identity
     */
    Identity findByUserEmailAndIdentityTypeName(String email, String typeName);
}
