package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.IActor;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.Tenant;

/**
 * Service interface for {@link IActor}
 */
public interface IActorService {

    /**
     * Saved new {@link Actor} to db
     * @param iOrderEvent orderEvent
     * @param userGuid user guid
     * @param orderGuid order guid
     * @return saved actor
     */
    IActor create(IOrderEvent iOrderEvent, String userGuid, String orderGuid);

}
