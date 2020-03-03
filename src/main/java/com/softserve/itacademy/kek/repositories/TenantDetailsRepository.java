package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.impl.TenantDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantDetailsRepository extends JpaRepository<TenantDetails, Long> {
}
