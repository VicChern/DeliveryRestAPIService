package com.softserve.itacademy.kek.services.impl;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderDetails;
import com.softserve.itacademy.kek.models.enums.ActorRoleEnum;
import com.softserve.itacademy.kek.models.enums.EventType;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderDetails;
import com.softserve.itacademy.kek.models.impl.OrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.repositories.OrderDetailsRepository;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(IOrderService.class);

    private final OrderRepository orderRepository;
    private final TenantRepository tenantRepository;
    private final ActorRoleRepository actorRoleRepository;
    private final OrderDetailsRepository orderDetailsRepository;
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
                            IOrderEventService orderEventService,
                            OrderDetailsRepository orderDetailsRepository) {
        this.orderRepository = orderRepository;
        this.tenantRepository = tenantRepository;
        this.actorRoleRepository = actorRoleRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.userService = userService;
        this.tenantService = tenantService;
        this.actorService = actorService;
        this.orderEventService = orderEventService;
    }

    @Transactional
    @Override
    public IOrder create(IOrder iOrder, UUID customerGuid) throws OrderServiceException {
        LOGGER.info("Saving Order to db: {}", iOrder);

        final User customer = (User) userService.getByGuid(customerGuid);
        final Tenant tenant = (Tenant) tenantService.getByGuid(iOrder.getTenant().getGuid());
        final Order actualOrder = transform(iOrder, tenant);
        final Order savedOrder;

        try {
            savedOrder = orderRepository.save(actualOrder);
            LOGGER.info("Order was saved: {}", savedOrder);
        } catch (PersistenceException e) {
            LOGGER.error("Order wasn`t saved: {}", actualOrder);
            throw new OrderServiceException("Order wasn`t saved");
        }

        final ActorRole actorRole = actorRoleRepository.findByName(ActorRoleEnum.CUSTOMER.toString());
        final Actor savedActor = actorService.saveActor(tenant, customer, actorRole);

        OrderEvent orderEvent = createOrderEvent();

        OrderEvent savedOrderEvent = (OrderEvent) orderEventService.createOrderEvent(savedOrder.getGuid(), savedActor.getUser().getGuid(), orderEvent);

        return savedOrder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<IOrder> getAll() throws OrderServiceException {
        LOGGER.info("Getting all Orders from db");
        final List<? extends IOrder> orderList;

        try {
            orderList = orderRepository.findAll();
        } catch (EntityNotFoundException e) {
            LOGGER.error("Orders weren't find");
            throw new OrderServiceException("Orders weren't find");
        }

        return (List<IOrder>) orderList;
    }

    @Transactional(readOnly = true)
    @Override
    public IOrder getByGuid(UUID guid) throws OrderServiceException {
        LOGGER.info("Getting Order from db by guid{}", guid);
        final Order order;

        try {
            order = orderRepository.findByGuid(guid);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Order with guid: {}, wasn`t found", guid);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t found");
        }

        LOGGER.info("Order with guid: {}, was found: {}", guid, order);
        return order;
    }

    @Transactional(readOnly = true)
    @Override
    public List<IOrder> getAllByTenantGuid(UUID guid) {
        LOGGER.info("Getting list of orders by tenant guid{}", guid);
        List<? extends IOrder> orders = orderRepository.findAllByTenantGuid(guid);
        LOGGER.info("return list of orders{}", orders);
        return (List<IOrder>) orders;


    }


    @Transactional
    @Override
    public IOrder update(IOrder order, UUID guid) {
        LOGGER.info("Updating order: {}, with guid: {}", order, guid);

        final Order actualOrder = orderRepository.findByGuid(guid);
        final Tenant actualTenant = tenantRepository.findByGuid(order.getTenant().getGuid()).get();
        final IOrderDetails orderDetails = order.getOrderDetails();
        final OrderDetails actualDetails = orderDetailsRepository.findByOrder(actualOrder);

        actualDetails.setPayload(orderDetails.getPayload());
        actualDetails.setImageUrl(orderDetails.getImageUrl());
        actualDetails.setOrder(actualOrder);

        actualOrder.setOrderDetails(actualDetails);
        actualOrder.setGuid(order.getGuid());
        actualOrder.setSummary(order.getSummary());
        actualOrder.setTenant(actualTenant);

        try {
            orderRepository.findByGuid(guid);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Order with guid: {}, wasn`t found", guid);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t found");
        }

        try {
            LOGGER.info("Order with guid: {}, was updated: {}", guid, actualOrder);
            return orderRepository.save(actualOrder);
        } catch (PersistenceException e) {
            LOGGER.error("Order with guid: {}, wasn`t updated: {}", guid, actualOrder);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t updated: " + actualOrder);
        }
    }

    @Transactional
    @Override
    public void deleteByGuid(UUID guid) throws OrderServiceException {
        LOGGER.info("Deleting Order from db by guid: {}", guid);

        final Order actualOrder;

        try {
            actualOrder = orderRepository.findByGuid(guid);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Order with guid: {}, wasn`t found", guid);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t found");
        }

        try {
            orderRepository.deleteById(actualOrder.getIdOrder());
        } catch (PersistenceException e) {
            LOGGER.error("Order wasn`t deleted from db: {}", actualOrder);
            throw new OrderServiceException("Order wasn`t deleted: " + actualOrder);
        }

        LOGGER.info("Order with guid: {}, was deleted", guid);
    }

    @Transactional
    private OrderEvent createOrderEvent() {
        OrderEvent orderEvent = new OrderEvent();

        OrderEventType orderEventType = new OrderEventType();
        orderEventType.setName(EventType.CREATED.toString());

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
