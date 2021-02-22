package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import com.softserve.itacademy.kek.exception.ActorServiceException;
import com.softserve.itacademy.kek.models.IActor;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.enums.ActorRoleEnum;

/**
 * Service interface for {@link IActor}
 */
public interface IActorService {

    /**
     * Creates actor
     *
     * @param tenant    tenant
     * @param user      user
     * @param actorRole role
     * @return created actor
     * @throws ActorServiceException if an error occurred
     */
    IActor create(ITenant tenant, IUser user, ActorRoleEnum actorRole) throws ActorServiceException;

    /**
     * Gets actor by guid
     *
     * @param guid guid
     * @return actor
     * @throws ActorServiceException if an error occurred
     */
    IActor getByGuid(UUID guid) throws ActorServiceException;

    /**
     * Returns a list of actors for tenant
     *
     * @param guid tenant guid
     * @return a list of actors for tenant
     * @throws ActorServiceException if an error occurred
     */
    List<IActor> getAllByTenantGuid(UUID guid) throws ActorServiceException;
}
