package com.softserve.itacademy.kek.services.impl;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.OrderEventServiceException;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.enums.ActorRoleEnum;
import com.softserve.itacademy.kek.models.enums.EventTypeEnum;
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

    private final static Logger logger = LoggerFactory.getLogger(IOrderEventService.class);

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
        logger.info("Insert order event into DB: orderGuid = {}", orderGuid);

        final OrderEvent orderEvent = new OrderEvent();

        final Order order = orderRepository.findByGuid(orderGuid);
        final boolean existingActor = actorRepository.findByGuid(iOrderEvent.getActor().getGuid()).isEmpty();

        if (existingActor) {
            final Actor actor = actorRepository.findByGuid(iOrderEvent.getActor().getGuid()).orElse(null);

            final OrderEventType orderEventType = orderEventTypeRepository.findByName(iOrderEvent.getOrderEventType().getName());

            orderEvent.setOrder(order);
            orderEvent.setGuid(iOrderEvent.getGuid());
            orderEvent.setActor(actor);
            orderEvent.setOrderEventType(orderEventType);
            orderEvent.setPayload(iOrderEvent.getPayload());
        }
        try {
            final OrderEvent insertedOrderEvent = orderEventRepository.saveAndFlush(orderEvent);

            logger.debug("Order event was inserted into DB: {}", insertedOrderEvent);

            return insertedOrderEvent;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while inserting order event in DB: " + orderEvent, ex);
            throw new OrderEventServiceException("An error occurred while inserting order event", ex);
        }
    }

    @Transactional
    @Override
    public IOrderEvent createOrderEvent(UUID orderGuid, UUID userGuid, IOrderEvent iOrderEvent) throws OrderEventServiceException {
        logger.info("Insert order event into DB: orderGuid = {}, userGuid = {}", orderGuid, userGuid);

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
            final OrderEvent insertedOrderEvent = orderEventRepository.saveAndFlush(orderEvent);

            logger.debug("Order event was inserted into DB: {}", insertedOrderEvent);

            return insertedOrderEvent;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while inserting order event in DB: " + orderEvent, ex);
            throw new OrderEventServiceException("An error occurred while inserting order event", ex);
        }
    }

    private Order getOrder(UUID orderGuid) {
        logger.info("Get order from DB: orderGuid = {}", orderGuid);

        try {
            final Order order = orderRepository.findByGuid(orderGuid);

            logger.debug("Order was read from DB: {}", order);

            return order;
        } catch (DataAccessException ex) {
            logger.error("Error while getting order from DB: orderGuid = " + orderGuid, ex);
            throw new OrderEventServiceException("An error occurred while getting order", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IOrderEvent getByGuid(UUID guid) throws OrderEventServiceException {
        logger.info("Get order event from DB: guid = {}", guid);

        try {
            final OrderEvent orderEvent = orderEventRepository.findByGuid(guid).orElseThrow(() -> {
                logger.error("Order event was not found in the DB: {}", guid);
                return new OrderEventServiceException("User was not found in database for guid: " + guid, new NoSuchElementException());
            });

            logger.debug("Order event was read from DB: {}", orderEvent);

            return orderEvent;

        } catch (DataAccessException ex) {
            logger.error("Error while getting order event from DB: guid = " + guid, ex);
            throw new OrderEventServiceException("An error occurred while getting order event", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IOrderEvent getLastAddedEvent(UUID orderGuid) throws OrderEventServiceException {
        logger.info("Get last added event: orderGuid = {}", orderGuid);

        final OrderEvent orderEvent;
        try {
            orderEvent = orderEventRepository
                    .findDistinctTopByOrderGuidOrderByLastModifiedDateDesc(orderGuid)
                    .orElseThrow();
        } catch (NoSuchElementException | DataAccessException ex) {
            logger.error("Error while getting last added event from DB: orderGuid = " + orderGuid, ex);
            throw new OrderEventServiceException("We can't provide information of this order", ex);
        }

        return orderEvent;
    }

    @Transactional
    @Override
    public Boolean ifOrderEventCanBeTracked(UUID orderGuid) throws OrderEventServiceException {
        logger.info("Check if order events can be tracked: orderGuid = {}", orderGuid);

        try {
            final Boolean started = orderEventRepository
                    .existsOrderEventsByOrderGuidAndOrderEventTypeName(orderGuid, EventTypeEnum.STARTED.toString());
            final Boolean delivered = orderEventRepository
                    .existsOrderEventsByOrderGuidAndOrderEventTypeName(orderGuid, EventTypeEnum.DELIVERED.toString());

            Boolean canBeTracked = started && !delivered;

            return canBeTracked;
        } catch (DataAccessException ex) {
            logger.error("Error while checking order events: orderGuid = " + orderGuid, ex);
            throw new OrderEventServiceException("An error occurred while checking order events", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<IOrderEvent> getAllEventsForOrder(UUID orderGuid) throws OrderEventServiceException {
        logger.info("Get order events from DB: orderGuid = {}", orderGuid);

        try {
            final Order order = orderRepository.findByGuid(orderGuid);
            final List<? extends IOrderEvent> orderEventList = orderEventRepository.getOrderEventsByOrder(order);

            logger.debug("Order events were read from DB: orderGuid = {}", orderGuid);

            return (List<IOrderEvent>) orderEventList;
        } catch (DataAccessException ex) {
            logger.error("Error while getting order event from DB: orderGuid = " + orderGuid, ex);
            throw new OrderEventServiceException("An error occurred while getting order event", ex);
        }
    }

    @Transactional
    @Override
    public List<IOrderEvent> findAllThatDeliveringNow() throws OrderEventServiceException {
        logger.info("Get all order events that delivering now");

        try {
            List<? extends IOrderEvent> orderEvents = orderEventRepository.findAllLastAddedOrderEventsForEventType(EventTypeEnum.STARTED.toString());

            logger.info("Order events that delivering now were read from DB");

            return (List<IOrderEvent>) orderEvents;
        } catch (DataAccessException ex) {
            logger.error("Error while getting order event that delivering now", ex);
            throw new OrderEventServiceException("An error occurred while getting order event that delivering now", ex);
        }
    }

    // ================================================== private ======================================================
    private OrderEventType getOrderEventType(String name) {
        final OrderEventType orderEventType = orderEventTypeRepository.findByName(name);

        if (orderEventType == null) {
            logger.error("OrderEventType wasn`t find for name: {}", name);
            throw new OrderEventServiceException("OrderEventType wasn`t find for name: " + name);
        }

        return orderEventType;
    }

    private Actor getActor(Tenant tenant, UUID actorGuid, String eventTypeName) {
        final Optional<Actor> actorByUserGuid = actorRepository.findByUserGuid(actorGuid);

        //if there isn`t actor for actorGuid or if its actor exist for another tenant
        if (actorByUserGuid.isEmpty() || !actorByUserGuid.get().getTenant().getGuid().equals(tenant.getGuid())) {

            return saveActor(tenant, actorGuid);

            //if actor for eventType ASSIGNED does`t have role CURRIER than save it with CURRIER role
        } else if (

                eventTypeName.equals(EventTypeEnum.ASSIGNED.toString())
                        &&
                        actorByUserGuid.get()
                                .getActorRoles()
                                .stream()
                                .noneMatch(actorRole -> actorRole.getName().equals(ActorRoleEnum.CURRIER.toString()))

        ) {

            return updateActorWithNewRole(actorByUserGuid.get(), ActorRoleEnum.CURRIER);

        }

        return actorByUserGuid.get();
    }

    private Actor saveActor(Tenant tenant, UUID userGuid) {
        final ActorRole actorRole = actorRoleRepository.findByName(ActorRoleEnum.CURRIER.toString());
        final User user = (User) userService.getByGuid(userGuid);

        return actorService.create(tenant, user, actorRole);
    }

    private Actor updateActorWithNewRole(Actor actor, ActorRoleEnum actorRoleEnum) {
        logger.info("Update actor: actor = {}, actorRoleEnum = {}", actor, actorRoleEnum);

        try {
            final ActorRole actorRole = actorRoleRepository.findByName(actorRoleEnum.toString());
            actor.addActorRole(actorRole);

            final Actor updatedActor = actorRepository.saveAndFlush(actor);

            logger.debug("Actor was updated in DB: actor = {}", updatedActor);

            return updatedActor;
        } catch (DataAccessException ex) {
            logger.error("Error while updating Actor in DB: " + actor, ex);
            throw new OrderEventServiceException("An error occurred while updating actor", ex);
        }
    }
}
