package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.impl.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
}
