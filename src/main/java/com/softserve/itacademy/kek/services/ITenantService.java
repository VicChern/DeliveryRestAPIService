package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import com.softserve.itacademy.kek.models.ITenant;

/**
 * Service interface for {@link ITenant}
 */
public interface ITenantService {

    /**
     * Saved new {@link ITenant} to db
     *
     * @param iTenant tenant
     * @return saved tenant
     */
    ITenant create(ITenant iTenant);

    /**
     * Gets all tenants for principal user
     *
     * @return all tenants for principal user
     */
    List<ITenant> getAll();

    /**
     * Gets tenant by {@link ITenant} guid
     *
     * @param guid {@link ITenant} guid
     * @return tenant
     */
    ITenant getByGuid(UUID guid);

    /**
     * Updates {@link ITenant}
     *
     * @param iTenant
     * @param guid   {@link ITenant} guid
     * @return updated tenant
     */
    ITenant update(ITenant iTenant, UUID guid);

    /**
     * Deletes {@link ITenant} by guid
     *
     * @param guid must not be {@literal null}.
     */
    void deleteByGuid(UUID guid);

}
