package com.softserve.itacademy.kek.repositories;

import com.softserve.itacademy.kek.models.TenantDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantDetailsRepository extends CrudRepository<TenantDetails, Integer> {
}
