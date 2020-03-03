package com.softserve.itacademy.kek.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IActorService;
import com.softserve.itacademy.kek.services.IOrderEventService;
import com.softserve.itacademy.kek.services.IOrderService;


/**
 * Service implementation for {@link IActorService}
 */
public class ActorServiceImpl implements IActorService {

    private final ActorRepository actorRepository;
    private final ActorRoleRepository actorRoleRepository;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final IOrderService orderService;
    private final IOrderEventService orderEventService;


    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository,
                            ActorRoleRepository actorRoleRepository,
                            UserRepository userRepository,
                            TenantRepository tenantRepository, IOrderService orderService, IOrderEventService orderEventService) {
        this.actorRepository = actorRepository;
        this.actorRoleRepository = actorRoleRepository;
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.orderService = orderService;
        this.orderEventService = orderEventService;
    }

}
