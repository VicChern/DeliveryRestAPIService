package com.softserve.itacademy.kek.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.ActorServiceException;
import com.softserve.itacademy.kek.models.IActor;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.enums.ActorRoleEnum;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.services.IActorRoleService;
import com.softserve.itacademy.kek.services.IActorService;
import com.softserve.itacademy.kek.services.ITenantService;
import com.softserve.itacademy.kek.services.IUserService;


/**
 * Service implementation for {@link IActorService}
 */
@Service
public class ActorServiceImpl implements IActorService {

    private final static Logger logger = LoggerFactory.getLogger(ActorServiceImpl.class);

    private final ActorRepository actorRepository;
    private final IActorRoleService actorRoleService;
    private final ITenantService tenantService;
    private final IUserService userService;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository,
                            IActorRoleService actorRoleService,
                            ITenantService tenantService,
                            IUserService userService) {
        this.actorRepository = actorRepository;
        this.actorRoleService = actorRoleService;
        this.tenantService = tenantService;
        this.userService = userService;
    }

    @Transactional
    @Override
    public IActor create(ITenant tenant, IUser user, ActorRoleEnum actorRole) throws ActorServiceException {
        logger.info("Insert actor into DB: tenant = {}, user = {}, actorRole = {}", tenant, user, actorRole);

        try {
            final Tenant actualTenant = (Tenant) tenantService.getByGuid(tenant.getGuid());
            final User actualUser = (User) userService.getByGuid(user.getGuid());
            final ActorRole role = (ActorRole) actorRoleService.getByName(actorRole.name());

            final Actor actor = new Actor();
            actor.setTenant(actualTenant);
            actor.setUser(actualUser);
            actor.setActorRoles(Collections.singletonList(role));
            actor.setGuid(UUID.randomUUID());
            actor.setAlias("Actor alias");

            final Actor insertedActor = actorRepository.saveAndFlush(actor);

            logger.debug("Actor was inserted into DB: {}", insertedActor);

            return insertedActor;
        } catch (Exception ex) {
            logger.error("Error while inserting actor into DB", ex);
            throw new ActorServiceException("An error occurred while inserting actor", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IActor getByGuid(UUID guid) throws ActorServiceException {
        logger.info("Get actor from DB by guid: {}", guid);

        try {
            final Actor actor = actorRepository.findByGuid(guid)
                    .orElseThrow(() -> new NoSuchElementException("Actor was not found in DB"));

            logger.debug("Actor was gotten from DB: {}", actor);

            return actor;
        } catch (Exception ex) {
            logger.error("Error while getting actor from DB by guid: " + guid, ex);
            throw new ActorServiceException("An error occurred while getting actor", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<IActor> getAllByTenantGuid(UUID guid) throws ActorServiceException {
        logger.info("Get all actors for tenant from DB: tenantGuid = {}", guid);

        try {
            final List<? extends IActor> actors = actorRepository.findAllByTenantGuid(guid);

            logger.debug("All actors for tenant was gotten from DB: tenantGuid = {}", guid);

            return (List<IActor>) actors;
        } catch (Exception ex) {
            logger.error("Error while getting all actors for tenant from DB: tenantGuid = " + guid, ex);
            throw new ActorServiceException("An error occurred while getting actors for tenant", ex);
        }
    }
}
