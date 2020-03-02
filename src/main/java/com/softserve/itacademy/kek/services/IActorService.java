package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;

/**
 * Service interface for {@link Actor}
 */
public interface IActorService {

    Actor saveActor(Tenant tenant, User user, ActorRole actorRole);
}
