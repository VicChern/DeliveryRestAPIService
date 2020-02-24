package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.exception.OrderEventServiceException;
import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderDetails;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.UUID;

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
    final Logger logger = LoggerFactory.getLogger(IOrderService.class);
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

        User customer = (User) userService.getByGuid(customerGuid);
        Tenant tenant = (Tenant) tenantService.getByGuid(iOrder.getTenant().getGuid());

        Order actualOrder = transform(iOrder, tenant);

        Order savedOrder;
        try {
            savedOrder = orderRepository.save(actualOrder);
        } catch (PersistenceException e) {
            logger.error("Order wasn`t saved: {}", actualOrder);
            throw new OrderServiceException("Order wasn`t saved");
        }

        ActorRole actorRole = actorRoleRepository.findByName(CUSTOMER);
        Actor savedActor = saveActor(tenant, customer, actorRole);

        createOrderEvent(savedOrder.getGuid(), savedActor.getGuid(), CREATED, "Create order event");

        logger.info("Order was saved: {}", savedOrder);
        return savedOrder;
    }

    @Transactional
    @Override
    public IOrderEvent createOrderEvent(UUID orderGuid, UUID userGuid, IOrderEvent iOrderEvent) {
        logger.info("Saving orderEvent for order: {} and actor : {}, orderEvent: {}", orderGuid, userGuid, iOrderEvent);

        return createOrderEvent(orderGuid, userGuid, iOrderEvent.getIdOrderEventType().getName(), iOrderEvent.getPayload());
    }


    private OrderEventType getOrderEventType(String name) {
        OrderEventType orderEventType = orderEventTypeRepository.findByName(name);
        if (orderEventType == null) {
            logger.error("OrderEventType wasn`t find for name: {}", name);
            throw new OrderServiceException("OrderEventType wasn`t find for name: " + name);
        }
        return orderEventType;
    }


    @Transactional(readOnly = true)
    @Override
    public Iterable<IOrder> getAll() throws OrderServiceException {
        logger.info("Getting all Orders from db");
        Iterable<? extends IOrder> orderList;

        try {
            orderList = orderRepository.findAll();
        } catch (EntityNotFoundException e) {
            logger.error("Orders weren't find");
            throw new OrderServiceException("Orders weren't find");
        }

        return (Iterable<IOrder>) orderList;
    }

    @Transactional(readOnly = true)
    @Override
    public IOrder getByGuid(UUID guid) throws OrderServiceException {
        logger.info("Getting Order from db by guid");
        Order order;

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

        Order actualOrder = orderRepository.findByGuid(guid);

        actualOrder.setOrderDetails((OrderDetails) order.getOrderDetails());
        actualOrder.setGuid(order.getGuid());
        actualOrder.setSummary(order.getSummary());
//        actualOrder.setTenant(order.getTenant());

        IOrderDetails details = order.getOrderDetails();
        OrderDetails actualDetails = new OrderDetails();

        if (details != null) {
//            actualDetails.setIdOrder(details.getIdOrder());
            actualDetails.setImageUrl(details.getImageUrl());
            actualDetails.setPayload(details.getPayload());

        }

        actualOrder.setOrderDetails(actualDetails);

        try {
            orderRepository.findByGuid(guid);
        } catch (EntityNotFoundException e) {
            logger.error("Order with guid: {}, wasn`t found", guid);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t found");
        }

        try {
            orderRepository.save(actualOrder);
        } catch (PersistenceException e) {
            logger.error("Order with guid: {}, wasn`t updated: {}", guid, actualOrder);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t updated: " + actualOrder);
        }

        logger.info("Order with guid: {}, was updated: {}", guid, actualOrder);
        return actualOrder;
    }

    @Transactional
    @Override
    public void deleteByGuid(UUID guid) throws OrderServiceException {
        logger.info("Deleting Order from db by guid: {}", guid);

        Order actualOrder;

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
        Order order = new Order();

        OrderDetails actualDetails = new OrderDetails();
        if (iOrder.getOrderDetails() != null) {
            actualDetails.setPayload(iOrder.getOrderDetails().getPayload());
            actualDetails.setImageUrl(iOrder.getOrderDetails().getImageUrl());
        }
        order.setOrderDetails(actualDetails);
        actualDetails.setOrder(order);
        order.setGuid(UUID.randomUUID());
        order.setSummary(iOrder.getSummary());
        order.setTenant(tenant);

        return order;
    }

    private Actor getActorByGuid(UUID guid) {
        Actor actor = actorRepository.findByGuid(guid);
        if (actor == null) {
            logger.error("Actor wasn`t find for guid: {}", guid);
            throw new OrderServiceException("Actor wasn`t find for guid: " + guid);
        }
        return actor;
    }

    private Actor saveActor(Tenant tenant, User user, ActorRole actorRole) {
        Actor actor = new Actor();
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
    private IOrderEvent createOrderEvent(UUID orderGuid, UUID userGuid, String orderEventTypeName, String payload) {
        logger.info("Saving orderEvent for order: {} and actor : {}", orderGuid, userGuid);

        Order order = (Order) getByGuid(orderGuid);
        Tenant tenant = (Tenant) tenantService.getByGuid(order.getTenant().getGuid());

        Actor actor = actorRepository.findByGuid(userGuid);
        // if actor doesn't exist yet
        if (actor == null || !actor.getTenant().getGuid().equals(tenant.getGuid())) {
            ActorRole actorRole = actorRoleRepository.findByName(CURRIER);
            User user = (User) userService.getByGuid(userGuid);
            actor = saveActor(tenant, user, actorRole);
        }

        OrderEventType orderEventType = getOrderEventType(orderEventTypeName);

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setIdOrder(order);
        orderEvent.setGuid(UUID.randomUUID());
        orderEvent.setIdActor(actor);
        orderEvent.setIdOrderEventType(orderEventType);
        orderEvent.setPayload(payload);

        try {
            orderEventRepository.save(orderEvent);
        } catch (PersistenceException e) {
            logger.error("OrderEvent for order: {}, actor: {}, orderEventType: {} wasn`t saved", order, actor, orderEventType);
            throw new OrderEventServiceException("OrderEvent for order: " + orderGuid + ", actor: " + userGuid + ", orderEventTypeName: " + orderEventTypeName + " wasn`t saved");
        }

        return orderEvent;
    }
}
