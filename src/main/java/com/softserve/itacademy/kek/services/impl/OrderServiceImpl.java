package com.softserve.itacademy.kek.services.impl;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderDetails;
import com.softserve.itacademy.kek.models.enums.ActorRoleEnum;
import com.softserve.itacademy.kek.models.enums.EventTypeEnum;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderDetails;
import com.softserve.itacademy.kek.models.impl.OrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.services.IActorService;
import com.softserve.itacademy.kek.services.IOrderEventService;
import com.softserve.itacademy.kek.services.IOrderService;
import com.softserve.itacademy.kek.services.ITenantService;
import com.softserve.itacademy.kek.services.IUserService;

/**
 * Service implementation for {@link IOrderService}
 */
@Service
public class OrderServiceImpl implements IOrderService {

    private final static Logger logger = LoggerFactory.getLogger(IOrderService.class);

    private final OrderRepository orderRepository;
    private final TenantRepository tenantRepository;
    private final ActorRoleRepository actorRoleRepository;
    private final IUserService userService;
    private final ITenantService tenantService;
    private final IActorService actorService;
    private final IOrderEventService orderEventService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            TenantRepository tenantRepository,
                            ActorRoleRepository actorRoleRepository,
                            IUserService userService,
                            ITenantService tenantService,
                            IActorService actorService,
                            IOrderEventService orderEventService) {
        this.orderRepository = orderRepository;
        this.tenantRepository = tenantRepository;
        this.actorRoleRepository = actorRoleRepository;
        this.userService = userService;
        this.tenantService = tenantService;
        this.actorService = actorService;
        this.orderEventService = orderEventService;
    }

    @Transactional
    @Override
    public IOrder create(IOrder iOrder, UUID customerGuid) throws OrderServiceException {
        logger.info("Insert order into DB: customerGuid = {}", customerGuid);

        final User customer = (User) userService.getByGuid(customerGuid);
        final Tenant tenant = (Tenant) tenantService.getByGuid(iOrder.getTenant().getGuid());
        final Order actualOrder = transform(iOrder, tenant);
        final Order savedOrder;

        try {
            savedOrder = orderRepository.saveAndFlush(actualOrder);

            logger.debug("Order was inserted into DB: {}", savedOrder);
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while inserting order into DB: " + actualOrder, ex);
            throw new OrderServiceException("An error occurred while inserting order", ex);
        }

        final ActorRole actorRole = actorRoleRepository.findByName(ActorRoleEnum.CUSTOMER.name()).orElse(null);
        final Actor savedActor = actorService.create(tenant, customer, actorRole);

        OrderEvent orderEvent = createOrderEvent();

        OrderEvent savedOrderEvent = (OrderEvent) orderEventService.createOrderEvent(savedOrder.getGuid(), savedActor.getUser().getGuid(), orderEvent);

        return savedOrder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<IOrder> getAll() throws OrderServiceException {
        logger.info("Get all orders from DB");

        try {
            final List<? extends IOrder> orderList = orderRepository.findAll();

            logger.debug("Orders were read from DB");

            return (List<IOrder>) orderList;
        } catch (DataAccessException ex) {
            logger.error("Error while getting orders from DB", ex);
            throw new OrderServiceException("An error occurred while getting orders", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IOrder getByGuid(UUID guid) throws OrderServiceException {
        logger.info("Get order from DB: guid = {}", guid);

        try {
            final Order order = orderRepository.findByGuid(guid);

            logger.debug("Order was found in DB: {}", order);

            return order;
        } catch (DataAccessException ex) {
            logger.error("Error while getting order from DB: " + guid, ex);
            throw new OrderServiceException("An error occurred while getting order", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<IOrder> getAllByTenantGuid(UUID guid) throws OrderServiceException {
        logger.info("Get a list of orders for tenant: tenantGuid = {}", guid);

        try {
            List<? extends IOrder> orders = orderRepository.findAllByTenantGuid(guid);

            logger.debug("A list of orders for tenant was read from DB: tenantGuid = {}", guid);

            return (List<IOrder>) orders;
        } catch (DataAccessException ex) {
            logger.error("Error while getting a list of orders for tenant from DB: tenantGuid = " + guid, ex);
            throw new OrderServiceException("An error occurred while getting orders", ex);
        }
    }

    @Transactional
    @Override
    public IOrder update(IOrder order, UUID guid) throws OrderServiceException {
        logger.info("Update order in DB: guid = {}", guid);

        final Order actualOrder;
        try {
            actualOrder = orderRepository.findByGuid(guid);
        } catch (DataAccessException e) {
            logger.error("Order with guid: {}, wasn`t found", guid);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t found");
        }

        final IOrderDetails orderDetails = order.getOrderDetails();
        final OrderDetails actualDetails = (OrderDetails) actualOrder.getOrderDetails();

        actualDetails.setPayload(orderDetails.getPayload());
        actualDetails.setImageUrl(orderDetails.getImageUrl());
        actualDetails.setOrder(actualOrder);

        actualOrder.setOrderDetails(actualDetails);
        actualOrder.setSummary(order.getSummary());

        try {
            Order updatedOrder = orderRepository.saveAndFlush(actualOrder);

            logger.debug("Order was updated in DB: {}", updatedOrder);

            return updatedOrder;
        } catch (ConstraintViolationException | DataAccessException ex) {
            logger.error("Error while updating order in DB: " + actualOrder, ex);
            throw new OrderServiceException("An error occurred while updating order", ex);
        }
    }

    @Transactional
    @Override
    public void deleteByGuid(UUID guid) throws OrderServiceException {
        logger.info("Delete order from DB: guid = {}", guid);

        final Order actualOrder;

        try {
            actualOrder = orderRepository.findByGuid(guid);
        } catch (DataAccessException ex) {
            logger.error("Error while getting order from DB: " + guid, ex);
            throw new OrderServiceException("An error occurred while getting order");
        }

        try {
            orderRepository.deleteById(actualOrder.getIdOrder());
            orderRepository.flush();

            logger.debug("Order was deleted from DB: {}", actualOrder);
        } catch (DataAccessException ex) {
            logger.error("Error while deleting order from DB: " + actualOrder, ex);
            throw new OrderServiceException("An error occurred while deleting order", ex);
        }
    }

    @Transactional
    private OrderEvent createOrderEvent() {
        OrderEvent orderEvent = new OrderEvent();

        OrderEventType orderEventType = new OrderEventType();
        orderEventType.setName(EventTypeEnum.CREATED.toString());

        orderEvent.setOrderEventType(orderEventType);
        orderEvent.setPayload("{lat: 0, lng: 0}");

        return orderEvent;
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
