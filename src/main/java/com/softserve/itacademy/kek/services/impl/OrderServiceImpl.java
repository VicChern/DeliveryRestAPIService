package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.modelInterfaces.IOrder;
import com.softserve.itacademy.kek.models.Order;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.services.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Service implementation for {@link IOrderService}
 */
@Service
public class OrderServiceImpl implements IOrderService {

    final Logger logger = LoggerFactory.getLogger(IOrderService.class);

    private final OrderRepository orderRepository;
    private final TenantRepository tenantRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, TenantRepository tenantRepository) {
        this.orderRepository = orderRepository;
        this.tenantRepository = tenantRepository;
    }

    @Override
    public IOrder save(IOrder order) throws OrderServiceException {
        logger.info("Saving Order to db: {}", order);
        UUID tenantGuid = order.getIdTenant().getGuid();

        try {
            tenantRepository.findByGuid(tenantGuid);
        } catch (EntityNotFoundException e) {
            logger.error("There is no tenant for order with tenant guid: {}", tenantGuid);
            throw new OrderServiceException("There is no tenant for order with tenant guid: " + tenantGuid);
        }

        try {
            orderRepository.save((Order) order);
        } catch (PersistenceException e) {
            logger.error("Order wasn`t saved: {}", order);
            throw new OrderServiceException("Order wasn`t saved");
        }

        logger.info("Order was saved: {}", order);
        return order;
    }

    @Override
    public Iterable<IOrder> getAll() throws OrderServiceException {
        logger.info("Getting all Orders from db");
        Iterable<Order> orderList = new ArrayList<>();

        try {
            orderList = orderRepository.findAll();
        } catch (EntityNotFoundException e) {
            logger.error("Orders weren't find");
            throw new OrderServiceException("Orders weren't find");
        }

        // return orderList;
        return null;
    }

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

    @Override
    public IOrder update(IOrder order, UUID guid) {
        logger.info("Updating order: {}, with guid: {}", order, guid);

        try {
            orderRepository.findByGuid(guid);
        } catch (EntityNotFoundException e) {
            logger.error("Order with guid: {}, wasn`t found", guid);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t found");
        }

        try {
            orderRepository.save((Order) order);
        } catch (PersistenceException e) {
            logger.error("Order with guid: {}, wasn`t updated: {}", guid, order);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t updated: " + order);
        }

        logger.info("Order with guid: {}, was updated: {}", guid, order);
        return order;
    }

    @Override
    public void deleteByGuid(UUID guid) throws OrderServiceException {
        logger.info("Deleting Order from db by guid");

        try {
            orderRepository.deleteByGuid(guid);
        } catch (EntityNotFoundException e) {
            logger.error("Order with guid: {}, wasn`t found", guid);
            throw new OrderServiceException("Order with guid: " + guid + ", wasn`t found");
        }

        logger.info("Order with guid: {}, was deleted", guid);
    }
}
