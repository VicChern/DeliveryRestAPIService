package com.softserve.itacademy.kek.services.impl;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.exception.ActorRoleServiceException;
import com.softserve.itacademy.kek.models.IActorRole;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.services.IActorRoleService;

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
        } catch (Exception ex) {
            logger.error("Error while getting actor role from DB", ex);
            throw new ActorRoleServiceException("An error occurred while getting actor role", ex);
        }

        if (actorRole.isEmpty()) {
            Exception ex = new NoSuchElementException();
            logger.error("Actor role was not found in DB", ex);
            throw new ActorRoleServiceException("Actor role was not found", ex);
        }

        logger.debug("Actor role was obtained from DB: {}", actorRole.get());

        return actorRole.get();
    }
}
