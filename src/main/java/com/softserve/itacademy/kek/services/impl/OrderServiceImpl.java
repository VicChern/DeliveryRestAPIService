package com.softserve.itacademy.kek.services.impl;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.OrderEventServiceException;
import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderDetails;
import com.softserve.itacademy.kek.models.impl.OrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.services.IOrderService;
import com.softserve.itacademy.kek.services.ITenantService;
import com.softserve.itacademy.kek.services.IUserService;

/**
 * Service implementation for {@link IOrderService}
 */
@Service
public class OrderServiceImpl implements IOrderService {

    private final static String CUSTOMER = "CUSTOMER";
    private final static String CURRIER = "CURRIER";
    private final static String CREATED = "CREATED";
    private final static String ASSIGNED = "ASSIGNED";
    private final static String STARTED = "STARTED";
    private final static String DELIVERED = "DELIVERED";
    private final Logger logger = LoggerFactory.getLogger(IOrderService.class);
    private final OrderRepository orderRepository;
    private final TenantRepository tenantRepository;
    private final OrderEventRepository orderEventRepository;
    private final ActorRepository actorRepository;
    private final OrderEventTypeRepository orderEventTypeRepository;
    private final ActorRoleRepository actorRoleRepository;
    private final IUserService userService;
    private final ITenantService tenantService;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            TenantRepository tenantRepository,
                            OrderEventRepository orderEventRepository,
                            ActorRepository actorRepository,
                            OrderEventTypeRepository orderEventTypeRepository,
                            ActorRoleRepository actorRoleRepository,
                            IUserService userService,
                            ITenantService tenantService) {
        this.orderRepository = orderRepository;
        this.tenantRepository = tenantRepository;
        this.orderEventRepository = orderEventRepository;
        this.actorRepository = actorRepository;
        this.orderEventTypeRepository = orderEventTypeRepository;
        this.actorRoleRepository = actorRoleRepository;
        this.userService = userService;
        this.tenantService = tenantService;
    }

    @Transactional
    @Override
    public IOrder create(IOrder iOrder, UUID customerGuid) throws OrderServiceException {
        logger.info("Saving Order to db: {}", iOrder);

        final User customer = (User) userService.getByGuid(customerGuid);
        final Tenant tenant = (Tenant) tenantService.getByGuid(iOrder.getTenant().getGuid());
        final Order actualOrder = transform(iOrder, tenant);
        final Order savedOrder;

        try {
            savedOrder = orderRepository.save(actualOrder);
            logger.info("Order was saved: {}", savedOrder);
        } catch (PersistenceException e) {
            logger.error("Order wasn`t saved: {}", actualOrder);
            throw new OrderServiceException("Order wasn`t saved");
        }

        createActorForOrder(tenant, customer, savedOrder);

        return savedOrder;
    }

    @Transactional(readOnly = true)
    public void createActorForOrder(Tenant tenant, User customer, Order savedOrder) {
        final ActorRole actorRole = actorRoleRepository.findByName(CUSTOMER);
        final Actor savedActor = saveActor(tenant, customer, actorRole);

        createOrderEvent(savedOrder.getGuid(), savedActor.getGuid(), CREATED, "Create order event");
    }

    @Transactional
    public Actor saveActor(Tenant tenant, User user, ActorRole actorRole) {
        final Actor actor = new Actor();

        actor.setTenant(tenant);
        actor.setUser(user);
        actor.setActorRoles(Collections.singletonList(actorRole));
        actor.setGuid(UUID.randomUUID());
        actor.setAlias("Actor created");

        try {
            return actorRepository.save(actor);
        } catch (PersistenceException e) {
            logger.error("Actor wasn`t saved for tenant: {}, user: {}, actorRole: {}", tenant, user, actorRole);
            throw new OrderServiceException("Actor wasn`t saved for tenant: " + tenant + ", user: " + user + ", actorRole: " + actorRole);
        }
    }

    @Transactional
    public IOrderEvent createOrderEvent(UUID orderGuid, UUID userGuid, String orderEventTypeName, String payload) {
        logger.info("Saving orderEvent for order: {} and actor : {}", orderGuid, userGuid);

        final Order order = (Order) getByGuid(orderGuid);
        final Tenant tenant = (Tenant) tenantService.getByGuid(order.getTenant().getGuid());

        Actor actor = actorRepository.findByGuid(userGuid);

        if (actor == null || !actor.getTenant().getGuid().equals(tenant.getGuid())) {
            ActorRole actorRole = actorRoleRepository.findByName(CURRIER);
            User user = (User) userService.getByGuid(userGuid);

            actor = saveActor(tenant, user, actorRole);
        }

        final OrderEventType orderEventType = getOrderEventType(orderEventTypeName);

        final OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrder(order);
        orderEvent.setGuid(UUID.randomUUID());
        orderEvent.setActor(actor);
        orderEvent.setOrderEventType(orderEventType);
        orderEvent.setPayload(payload);

        try {
            return orderEventRepository.save(orderEvent);
        } catch (PersistenceException e) {
            logger.error("OrderEvent for order: {}, actor: {}, orderEventType: {} wasn`t saved", order, actor, orderEventType);
            throw new OrderEventServiceException("OrderEvent for order: " + orderGuid + ", actor: " + userGuid + ", orderEventTypeName: " + orderEventTypeName + " wasn`t saved");
        }
    }

    @Transactional(readOnly = true)
    public OrderEventType getOrderEventType(String name) {
        final OrderEventType orderEventType = orderEventTypeRepository.findByName(name);

        if (orderEventType == null) {
            logger.error("OrderEventType wasn`t find for name: {}", name);
            throw new OrderServiceException("OrderEventType wasn`t find for name: " + name);
        }

        return orderEventType;
    }


    @Transactional(readOnly = true)
    @Override
    public List<IOrder> getAll() throws OrderServiceException {
        logger.info("Getting all Orders from db");
        final Iterable<? extends IOrder> orderList;

        try {
            orderList = orderRepository.findAll();
        } catch (EntityNotFoundException e) {
            logger.error("Orders weren't find");
            throw new OrderServiceException("Orders weren't find");
        }

        return (List<IOrder>) orderList;
    }

    @Transactional(readOnly = true)
    @Override
    public IOrder getByGuid(UUID guid) throws OrderServiceException {
        logger.info("Getting Order from db by guid");
        final Order order;

        try {
            order = orderRepository.findByGuid(guid);
        } catch (EntityNotFoundException e) {
            logger.error("Order with guid: {}, wasn`t found", guid);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t found");
        }

        logger.info("Order with guid: {}, was found: {}", guid, order);
        return order;
    }

    @Transactional
    @Override
    public IOrder update(IOrder order, UUID guid) {
        logger.info("Updating order: {}, with guid: {}", order, guid);

        final Order actualOrder = orderRepository.findByGuid(guid);
        final Tenant actualTenant = tenantRepository.findByGuid(order.getTenant().getGuid()).get();

        actualOrder.setGuid(order.getGuid());
        actualOrder.setSummary(order.getSummary());
        actualOrder.setTenant(actualTenant);

        try {
            orderRepository.findByGuid(guid);
        } catch (EntityNotFoundException e) {
            logger.error("Order with guid: {}, wasn`t found", guid);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t found");
        }

        try {
            logger.info("Order with guid: {}, was updated: {}", guid, actualOrder);
            return orderRepository.save(actualOrder);
        } catch (PersistenceException e) {
            logger.error("Order with guid: {}, wasn`t updated: {}", guid, actualOrder);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t updated: " + actualOrder);
        }
    }

    @Transactional
    @Override
    public void deleteByGuid(UUID guid) throws OrderServiceException {
        logger.info("Deleting Order from db by guid: {}", guid);

        final Order actualOrder;

        try {
            actualOrder = orderRepository.findByGuid(guid);
        } catch (EntityNotFoundException e) {
            logger.error("Order with guid: {}, wasn`t found", guid);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t found");
        }

        try {
            orderRepository.deleteById(actualOrder.getIdOrder());
        } catch (PersistenceException e) {
            logger.error("Order wasn`t deleted from db: {}", actualOrder);
            throw new OrderServiceException("Order wasn`t deleted: " + actualOrder);
        }

        logger.info("Order with guid: {}, was deleted", guid);
    }


    private Order transform(IOrder iOrder, Tenant tenant) {
        final Order order = new Order();

        OrderDetails actualDetails = new OrderDetails();

        if (iOrder.getOrderDetails() != null) {
            actualDetails.setPayload(iOrder.getOrderDetails().getPayload());
            actualDetails.setImageUrl(iOrder.getOrderDetails().getImageUrl());
        }

        order.setOrderDetails(actualDetails);
        order.setGuid(UUID.randomUUID());
        order.setSummary(iOrder.getSummary());
        order.setTenant(tenant);

        actualDetails.setOrder(order);

        return order;
    }
}
