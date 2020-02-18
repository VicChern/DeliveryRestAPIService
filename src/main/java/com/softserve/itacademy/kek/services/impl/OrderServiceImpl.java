package com.softserve.itacademy.kek.services.impl;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.UUID;

import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.services.ITenantService;
import com.softserve.itacademy.kek.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.OrderEventServiceException;
import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderDetails;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderDetails;
import com.softserve.itacademy.kek.models.impl.OrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.services.IOrderService;

/**
 * Service implementation for {@link IOrderService}
 */
@Service
public class OrderServiceImpl implements IOrderService {

    final Logger logger = LoggerFactory.getLogger(IOrderService.class);

    private final OrderRepository orderRepository;
    private final TenantRepository tenantRepository;
    private final OrderEventRepository orderEventRepository;
    private final ActorRepository actorRepository;
    private final OrderEventTypeRepository orderEventTypeRepository;
    private final IUserService userService;
    private final ITenantService tenantService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            TenantRepository tenantRepository,
                            OrderEventRepository orderEventRepository,
                            ActorRepository actorRepository,
                            OrderEventTypeRepository orderEventTypeRepository,
                            IUserService userService,
                            ITenantService tenantService) {
        this.orderRepository = orderRepository;
        this.tenantRepository = tenantRepository;
        this.orderEventRepository = orderEventRepository;
        this.actorRepository = actorRepository;
        this.orderEventTypeRepository = orderEventTypeRepository;
        this.userService = userService;
        this.tenantService = tenantService;
    }

    @Transactional
    @Override
    public IOrder create(IOrder iOrder, UUID customerGuid) throws OrderServiceException {
        logger.info("Saving Order to db: {}", iOrder);

        User customer = (User) userService.getByGuid(customerGuid);
        Tenant tenant = (Tenant) tenantService.getByGuid(iOrder.getTenant().getGuid());

        Order actualOrder = new Order();

        OrderDetails actualDetails = new OrderDetails();
        if (iOrder.getOrderDetails() != null) {
            actualDetails.setPayload(iOrder.getOrderDetails().getPayload());
            actualDetails.setImageUrl(iOrder.getOrderDetails().getImageUrl());
        }
        actualOrder.setOrderDetails(actualDetails);
        actualDetails.setOrder(actualOrder);
        actualOrder.setGuid(UUID.randomUUID());
        actualOrder.setSummary(iOrder.getSummary());
        actualOrder.setTenant(tenant);

        Order savedOrder;
        try {
            savedOrder = orderRepository.save(actualOrder);
        } catch (PersistenceException e) {
            logger.error("Order wasn`t saved: {}", actualOrder);
            throw new OrderServiceException("Order wasn`t saved");
        }

        Actor actor = new Actor();
        actor.setTenant(tenant);
        actor.setUser(customer);
//        actor.set

//        createOrderEvent(savedOrder);


        logger.info("Order was saved: {}", actualOrder);
        return savedOrder;
    }

    @Transactional
    public void createOrderEvent(IOrder order) {
        logger.info("Saving orderEvent for order: {}", order);

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setIdOrder((Order) order);
        orderEvent.setGuid(UUID.randomUUID());
        orderEvent.setPayload("Created order");

        OrderEventType orderEventType = new OrderEventType();

        orderEventType.setName("CREATED");
//        orderEventType.setIdOrderEventType(1L);

        orderEventTypeRepository.save(orderEventType);

        orderEvent.setIdOrderEventType(orderEventType);

        try {
            orderEventRepository.save(orderEvent);
        } catch (PersistenceException e) {
            logger.error("OrderEvent for Order: {} wasn`t save", order);
            throw new OrderEventServiceException("OrderEvent for Order: " + order + " wasn`t save");
        }
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
            actualDetails.setIdOrder(details.getIdOrder());
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
}
