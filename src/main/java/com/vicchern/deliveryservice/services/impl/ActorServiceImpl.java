package com.vicchern.deliveryservice.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.vicchern.deliveryservice.exception.ActorServiceException;
import com.vicchern.deliveryservice.models.IActor;
import com.vicchern.deliveryservice.models.ITenant;
import com.vicchern.deliveryservice.models.IUser;
import com.vicchern.deliveryservice.models.enums.ActorRoleEnum;
import com.vicchern.deliveryservice.models.impl.Actor;
import com.vicchern.deliveryservice.models.impl.ActorRole;
import com.vicchern.deliveryservice.models.impl.Tenant;
import com.vicchern.deliveryservice.models.impl.User;
import com.vicchern.deliveryservice.repositories.ActorRepository;
import com.vicchern.deliveryservice.services.IActorRoleService;
import com.vicchern.deliveryservice.services.IActorService;
import com.vicchern.deliveryservice.services.ITenantService;
import com.vicchern.deliveryservice.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
            actor.setAlias(actorRole.name());

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
