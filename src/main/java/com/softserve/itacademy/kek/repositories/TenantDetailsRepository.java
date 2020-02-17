package com.softserve.itacademy.kek.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.itacademy.kek.models.TenantDetails;

public interface TenantDetailsRepository extends JpaRepository<TenantDetails, Long> {
}
