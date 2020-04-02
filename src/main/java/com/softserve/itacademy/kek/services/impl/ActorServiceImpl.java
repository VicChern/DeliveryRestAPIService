package com.softserve.itacademy.kek.services.impl;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.ActorServiceException;
import com.softserve.itacademy.kek.models.IActor;
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

    private final static Logger logger = LoggerFactory.getLogger(ActorServiceImpl.class);

    private final ActorRepository actorRepository;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Transactional
    @Override
    public Actor create(Tenant tenant, User user, ActorRole actorRole) throws ActorServiceException {
        logger.info("Insert actor into DB: tenantGuid = {}, userGuid = {}, roleName = {}",
                tenant.getGuid(), user.getGuid(), actorRole.getName());

        final Actor actor = new Actor();

        actor.setTenant(tenant);
        actor.setUser(user);
        actor.setActorRoles(Collections.singletonList(actorRole));
        actor.setGuid(UUID.randomUUID());
        actor.setAlias("Actor alias");

        try {
            final Actor insertedActor = actorRepository.saveAndFlush(actor);

            logger.debug("Actor was inserted into DB: tenantGuid = {}, userGuid = {}, insertedActor = {}",
                    tenant.getGuid(), user.getGuid(), insertedActor);

            return insertedActor;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while inserting actor into DB: actor = " + actor, ex);
            throw new ActorServiceException("An error occurred while inserting actor", ex);
        }
    }

    @Transactional
    @Override
    public List<IActor> getAllByTenantGuid(UUID guid) throws ActorServiceException {
        logger.info("Get a list of actors for tenant from DB: tenantGuid = {}", guid);

        try {
            final List<? extends IActor> actors = actorRepository.findAllByTenantGuid(guid);

            logger.debug("A list of actors for tenant was gotten from DB: tenantGuid = {}", guid);

            return (List<IActor>) actors;
        } catch (DataAccessException ex) {
            logger.error("Error while getting a list of actors for tenant from DB: tenantGuid = " + guid, ex);
            throw new ActorServiceException("An error occurred while getting actors for tenant", ex);
        }
    }
}
