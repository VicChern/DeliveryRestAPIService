package com.softserve.itacademy.kek.services.impl;

import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.services.IActorService;


/**
 * Service implementation for {@link IActorService}
 */
@Service
public class ActorServiceImpl implements IActorService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ActorServiceImpl.class);

    private final ActorRepository actorRepository;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Transactional
    @Override
    public Actor saveActor(Tenant tenant, User user, ActorRole actorRole) {
        final Actor actor = new Actor();

        actor.setTenant(tenant);
        actor.setUser(user);
        actor.setActorRoles(Collections.singletonList(actorRole));
        actor.setGuid(UUID.randomUUID());
        actor.setAlias("Actor alias");

        try {
            return actorRepository.save(actor);
        } catch (PersistenceException e) {
            LOGGER.error("Actor wasn`t saved for tenant: {}, user: {}, actorRole: {}", tenant, user, actorRole);
            throw new OrderServiceException("Actor wasn`t saved for tenant: " + tenant + ", user: " + user + ", actorRole: " + actorRole);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Actor getAllByUserGuid(UUID guid) {
        LOGGER.info("Getting actor by user guid{}", guid);
        Actor actor = new Actor();
        if (actorRepository.findByUserGuid(guid).isPresent())
        actor = actorRepository.findByUserGuid(guid).get();

        return actor;
    }
}
