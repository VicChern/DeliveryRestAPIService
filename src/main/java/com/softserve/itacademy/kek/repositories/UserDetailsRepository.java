package com.softserve.itacademy.kek.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.UserDetails;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
}
