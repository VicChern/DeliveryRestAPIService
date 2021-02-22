package com.vicchern.deliveryservice.services.impl;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.vicchern.deliveryservice.exception.ActorRoleServiceException;
import com.vicchern.deliveryservice.models.IActorRole;
import com.vicchern.deliveryservice.models.impl.ActorRole;
import com.vicchern.deliveryservice.repositories.ActorRoleRepository;
import com.vicchern.deliveryservice.services.IActorRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActorRoleServiceImpl implements IActorRoleService {

    private final static Logger logger = LoggerFactory.getLogger(ActorRoleServiceImpl.class);

    private final ActorRoleRepository actorRoleRepository;

    @Autowired
    public ActorRoleServiceImpl(ActorRoleRepository actorRoleRepository) {
        this.actorRoleRepository = actorRoleRepository;
    }

    @Override
    public IActorRole getByName(String name) throws ActorRoleServiceException {
        logger.info("Get actor role from DB by name: {}", name);

        final Optional<ActorRole> actorRole;

        try {
            actorRole = actorRoleRepository.findByName(name);

            logger.debug("Actor role was read from DB: {}", actorRole);
        } catch (Exception ex) {
            logger.error("Error while getting actor role from DB", ex);
            throw new ActorRoleServiceException("An error occurred while getting actor role", ex);
        }

        if (actorRole.isEmpty()) {
            Exception ex = new NoSuchElementException();
            logger.error("Actor role was not found in DB", ex);
            throw new ActorRoleServiceException("Actor role was not found", ex);
        }

        return actorRole.get();
    }
}
