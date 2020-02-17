package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.dataexchange.IOrder;
import com.softserve.itacademy.kek.dataexchange.IOrderDetails;
import com.softserve.itacademy.kek.exception.OrderEventServiceException;
import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.models.Order;
import com.softserve.itacademy.kek.models.OrderDetails;
import com.softserve.itacademy.kek.models.OrderEvent;
import com.softserve.itacademy.kek.models.OrderEventType;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.services.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.UUID;

/**
 * Service implementation for {@link IOrderService}
 */
@Service
public class OrderServiceImpl implements IOrderService {

    final Logger logger = LoggerFactory.getLogger(IOrderService.class);

    private final OrderRepository orderRepository;
    private final TenantRepository tenantRepository;
    private final OrderEventRepository orderEventRepository;
    private final OrderEventTypeRepository orderEventTypeRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, TenantRepository tenantRepository, OrderEventRepository orderEventRepository, OrderEventTypeRepository orderEventTypeRepository) {
        this.orderRepository = orderRepository;
        this.tenantRepository = tenantRepository;
        this.orderEventRepository = orderEventRepository;
        this.orderEventTypeRepository = orderEventTypeRepository;
    }

    @Transactional
    @Override
    public IOrder create(IOrder order) throws OrderServiceException {
        logger.info("Saving Order to db: {}", order);

        Order actualOrder = new Order();

        actualOrder.setOrderDetails((OrderDetails) order.getOrderDetails());
        actualOrder.setGuid(order.getGuid());
        actualOrder.setSummary(order.getSummary());
        actualOrder.setTenant(order.getTenant());

        UUID tenantGuid = actualOrder.getTenant().getGuid();

        OrderDetails actualDetails = new OrderDetails();
        IOrderDetails details = actualOrder.getOrderDetails();

        if (details != null) {
            actualDetails.setPayload(details.getPayload());
            actualDetails.setImageUrl(details.getImageUrl());
        }

        actualOrder.setOrderDetails(actualDetails);
        actualDetails.setOrder(actualOrder);

        try {
            tenantRepository.findByGuid(tenantGuid);
        } catch (EntityNotFoundException e) {
            logger.error("There is no tenant for order with tenant guid: {}", tenantGuid);
            throw new OrderServiceException("There is no tenant for order with tenant guid: " + tenantGuid);
        }

        try {
            orderRepository.save(actualOrder);
        } catch (PersistenceException e) {
            logger.error("Order wasn`t saved: {}", actualOrder);
            throw new OrderServiceException("Order wasn`t saved");
        }

        createOrderEvent(actualOrder);

        logger.info("Order was saved: {}", actualOrder);
        return actualOrder;
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
        actualOrder.setTenant(order.getTenant());

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
