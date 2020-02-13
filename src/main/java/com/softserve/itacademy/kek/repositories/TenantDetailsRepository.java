package com.softserve.itacademy.kek.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softserve.itacademy.kek.models.TenantDetails;

public interface TenantDetailsRepository extends CrudRepository<TenantDetails, Long> {
}
