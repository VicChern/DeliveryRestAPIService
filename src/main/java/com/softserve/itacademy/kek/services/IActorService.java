package com.softserve.itacademy.kek.services;

import java.util.List;
import java.util.UUID;

import com.softserve.itacademy.kek.exception.ActorServiceException;
import com.softserve.itacademy.kek.models.IActor;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;

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
     * @throws ActorServiceException
     */
    Actor create(Tenant tenant, User user, ActorRole actorRole) throws ActorServiceException;

    /**
     * Returns a list of actors for tenant
     *
     * @param guid tenant guid
     * @return a list of actors for tenant
     * @throws ActorServiceException
     */
    List<IActor> getAllByTenantGuid(UUID guid) throws ActorServiceException;
}
