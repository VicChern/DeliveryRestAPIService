package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.impl.Identity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityRepository extends JpaRepository<Identity, Long> {
}
