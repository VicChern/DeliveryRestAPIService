package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.exception.ActorRoleServiceException;
import com.softserve.itacademy.kek.models.IActorRole;

/**
 * Service to work with actor role
 */
public interface IActorRoleService {
    /**
     * Gets an actor role by name
     *
     * @param name name
     * @return actor role
     * @throws ActorRoleServiceException
     */
    IActorRole getByName(String name) throws ActorRoleServiceException;
}
