package com.softserve.itacademy.kek.services.impl;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.OrderEventServiceException;
import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.enums.ActorRoleEnum;
import com.softserve.itacademy.kek.models.enums.EventType;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.services.IActorService;
import com.softserve.itacademy.kek.services.IOrderEventService;
import com.softserve.itacademy.kek.services.ITenantService;
import com.softserve.itacademy.kek.services.IUserService;

/**
 * Service implementation for {@link IOrderEventService}
 */
@Service
public class OrderEventServiceImpl implements IOrderEventService {

    private final static Logger LOGGER = LoggerFactory.getLogger(IOrderEventService.class);

    private final OrderEventRepository orderEventRepository;
    private final OrderEventTypeRepository orderEventTypeRepository;
    private final OrderRepository orderRepository;
    private final ActorRepository actorRepository;
    private final ActorRoleRepository actorRoleRepository;

    private final IUserService userService;
    private final ITenantService tenantService;
    private final IActorService actorService;

    @Autowired
    public OrderEventServiceImpl(OrderEventRepository orderEventRepository,
                                 OrderRepository orderRepository,
                                 ActorRepository actorRepository,
                                 OrderEventTypeRepository orderEventTypeRepository,
                                 ActorRoleRepository actorRoleRepository,
                                 IUserService userService,
                                 ITenantService tenantService,
                                 IActorService actorService) {
        this.orderEventRepository = orderEventRepository;
        this.orderEventTypeRepository = orderEventTypeRepository;
        this.orderRepository = orderRepository;
        this.actorRepository = actorRepository;
        this.actorRoleRepository = actorRoleRepository;
        this.userService = userService;
        this.tenantService = tenantService;
        this.actorService = actorService;
    }

    @Transactional
    @Override
    public IOrderEvent create(IOrderEvent iOrderEvent, UUID orderGuid) throws OrderEventServiceException {
        LOGGER.info("Saving OrderEvent to db: {}", iOrderEvent);
        final OrderEvent orderEvent = new OrderEvent();

        final Order order = orderRepository.findByGuid(orderGuid);
        final Actor actor = actorRepository.findByGuid(iOrderEvent.getActor().getGuid());
        final OrderEventType orderEventType = orderEventTypeRepository.findByName(iOrderEvent.getOrderEventType().getName());


        orderEvent.setOrder(order);
        orderEvent.setGuid(iOrderEvent.getGuid());
        orderEvent.setActor(actor);
        orderEvent.setOrderEventType(orderEventType);
        orderEvent.setPayload(iOrderEvent.getPayload());

        try {
            orderEventRepository.save(orderEvent);
        } catch (PersistenceException e) {
            LOGGER.error("Order event wasn`t saved: {}", orderEvent);
            throw new OrderServiceException("Order event wasn`t saved");
        }

        LOGGER.info("Order event was saved: {}", orderEvent);
        return orderEvent;
    }

    @Transactional
    @Override
    public IOrderEvent createOrderEvent(UUID orderGuid, UUID userGuid, IOrderEvent iOrderEvent) {
        LOGGER.info("Saving orderEvent for order: {} and actor : {}, orderEvent: {}", orderGuid, userGuid, iOrderEvent);

        final Order order = getOrder(orderGuid);
        final Tenant tenant = (Tenant) tenantService.getByGuid(order.getTenant().getGuid());
        final Actor actor = getActor(tenant, userGuid, iOrderEvent.getOrderEventType().getName());

        final OrderEventType orderEventType = getOrderEventType(iOrderEvent.getOrderEventType().getName());

        final OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrder(order);
        orderEvent.setGuid(UUID.randomUUID());
        orderEvent.setActor(actor);
        orderEvent.setOrderEventType(orderEventType);
        orderEvent.setPayload(iOrderEvent.getPayload());

        try {
            return orderEventRepository.save(orderEvent);
        } catch (PersistenceException e) {
            LOGGER.error("OrderEvent for order: {}, actor: {}, orderEventType: {} wasn`t saved", order, actor, orderEventType);
            throw new OrderEventServiceException("OrderEvent for order: " + orderGuid + ", actor: " + userGuid + ", orderEvent: " + orderEvent + " wasn`t saved");
        }
    }

    private Order getOrder(UUID orderGuid) {
        LOGGER.info("Getting Order from db by guid");
        final Order order;

        try {
            order = orderRepository.findByGuid(orderGuid);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Order with guid: {}, wasn`t found", orderGuid);
            throw new OrderServiceException("Order with guid: " + orderGuid + ", wasn`t found");
        }

        LOGGER.info("Order with guid: {}, was found: {}", orderGuid, order);
        return order;
    }

    @Transactional(readOnly = true)
    @Override
    public IOrderEvent getByGuid(UUID guid) throws OrderEventServiceException {
        LOGGER.info("Getting OrderEvent from db by guid");
        final OrderEvent orderEvent;

        try {
            orderEvent = orderEventRepository.findByGuid(guid);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Order event with guid: {}, wasn`t found", guid);
            throw new OrderServiceException("Order event with guid: " + guid + ", wasn`t found");
        }

        LOGGER.info("Order event with guid: {}, was found: {}", guid, orderEvent);
        return orderEvent;
    }

    @Transactional(readOnly = true)
    @Override
    public IOrderEvent getLastAddedEvent(UUID orderGuid) {
        final OrderEvent orderEvent;
        try {
            orderEvent = orderEventRepository
                    .findDistinctTopByOrderGuidOrderByLastModifiedDateDesc(orderGuid)
                    .orElseThrow();
        } catch (NoSuchElementException ex) {
            throw new OrderEventServiceException("We can't provide information of this order");
        }

        return orderEvent;
    }

    @Transactional
    @Override
    public Boolean ifOrderEventCanBeTracked(UUID orderGuid) {

        Boolean started = orderEventRepository
                .existsOrderEventsByOrderGuidAndOrderEventTypeName(orderGuid, EventType.STARTED.toString());
        Boolean delivered = orderEventRepository
                .existsOrderEventsByOrderGuidAndOrderEventTypeName(orderGuid, EventType.DELIVERED.toString());


        Boolean canBeTracked = started && !delivered;

        return canBeTracked;
    }

    @Transactional(readOnly = true)
    @Override
    public List<IOrderEvent> getAllEventsForOrder(UUID orderGuid) {
        LOGGER.info("Getting OrderEvent from db by Order");

        Order order = orderRepository.findByGuid(orderGuid);

        final List<? extends IOrderEvent> orderEventList;

        try {
            orderEventList = orderEventRepository.getOrderEventsByOrder(order);
        } catch (Exception e) {
            LOGGER.error("An error occurred: {}", e.toString());
            throw e;
        }

        return (List<IOrderEvent>) orderEventList;
    }

    @Transactional
    @Override
    public List<IOrderEvent> findAllThatDeliveringNow() {
        LOGGER.debug("Getting all order events that delivering now");

        List<? extends IOrderEvent> orderEvents = orderEventRepository.findAllLastAddedOrderEventsForEventType(EventType.STARTED.toString());

        return (List<IOrderEvent>) orderEvents;
    }

    // ================================================== private ======================================================
    private OrderEventType getOrderEventType(String name) {
        final OrderEventType orderEventType = orderEventTypeRepository.findByName(name);

        if (orderEventType == null) {
            LOGGER.error("OrderEventType wasn`t find for name: {}", name);
            throw new OrderServiceException("OrderEventType wasn`t find for name: " + name);
        }

        return orderEventType;
    }

    private Actor getActor(Tenant tenant, UUID actorGuid, String eventTypeName) {
        final Actor actor = actorRepository.findByGuid(actorGuid);

        //if there isn`t actor for actorGuid
        if (actor == null || !actor.getTenant().getGuid().equals(tenant.getGuid())) {
            return saveActor(tenant, actorGuid);
        }

        //if actor for eventType ASSIGNED doesn`t have role CURRIER than save it with CURRIER role
        if (
                eventTypeName.equals(EventType.ASSIGNED.toString())
                        &&
                        actor.getActorRoles()
                                .stream()
                                .noneMatch(actorRole -> actorRole.getName().equals(ActorRoleEnum.CURRIER.toString()))
        ) {

            return updateActorWithNewRole(actor, ActorRoleEnum.CURRIER);
        }

        return actor;
    }

    private Actor saveActor(Tenant tenant, UUID userGuid) {
        final ActorRole actorRole = actorRoleRepository.findByName(ActorRoleEnum.CURRIER.toString());
        final User user = (User) userService.getByGuid(userGuid);

        return actorService.saveActor(tenant, user, actorRole);
    }

    private Actor updateActorWithNewRole(Actor actor, ActorRoleEnum actorRoleEnum) {
        final ActorRole actorRole = actorRoleRepository.findByName(actorRoleEnum.toString());
        actor.addActorRole(actorRole);
        try {
            return actorRepository.save(actor);
        } catch (PersistenceException e) {
            LOGGER.error("Actor wasn`t saved : {}", actor);
            throw new OrderServiceException("Actor wasn`t saved : " + actor);
        }
    }
}
