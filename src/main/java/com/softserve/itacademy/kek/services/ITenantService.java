package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.softserve.itacademy.kek.exception.TenantServiceException;
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
     * @throws TenantServiceException if tenant wasn't saved
     */
    ITenant create(ITenant iTenant) throws TenantServiceException;

    /**
     * Gets tenant by {@link ITenant} guid
     *
     * @param guid {@link ITenant} guid
     * @return tenant
     * @throws TenantServiceException if there is no one tenant with guid
     */
    ITenant getByGuid(UUID guid) throws TenantServiceException;

    /**
     * Gets all tenants for principal user
     *
     * @return all tenants for principal user
     * @throws TenantServiceException
     */
    List<ITenant> getAll() throws TenantServiceException;

    /**
     * Gets a {@link Page} of {@link ITenant}
     * meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable {@code Pageable} object that defines page options, must not be {@literal null}.
     * @return a page of {@link ITenant}
     * @throws TenantServiceException
     */
    Page<ITenant> getAllPageable(Pageable pageable) throws TenantServiceException;

    /**
     * Updates {@link ITenant}
     *
     * @param iTenant iTenant
     * @param guid    {@link ITenant} guid
     * @return updated tenant
     * @throws TenantServiceException if if tenant wasn't updated
     */
    ITenant update(ITenant iTenant, UUID guid) throws TenantServiceException;

    /**
     * Deletes {@link ITenant} by guid
     *
     * @param guid must not be {@literal null}.
     * @throws TenantServiceException if there is no one tenant with guid for deleting
     */
    void deleteByGuid(UUID guid) throws TenantServiceException;

}
