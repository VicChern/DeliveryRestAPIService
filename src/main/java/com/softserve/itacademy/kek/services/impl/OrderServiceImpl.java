package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.dataexchange.IOrder;
import com.softserve.itacademy.kek.dataexchange.IOrderDetails;
import com.softserve.itacademy.kek.models.Order;
import com.softserve.itacademy.kek.models.OrderDetails;
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

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, TenantRepository tenantRepository) {
        this.orderRepository = orderRepository;
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    @Override
    public IOrder create(IOrder order) throws OrderServiceException {
        logger.info("Saving Order to db: {}", order);

        Order actualOrder = new Order();

        actualOrder = (Order) order;

        UUID tenantGuid = actualOrder.getIdTenant().getGuid();

        OrderDetails actualDetails = new OrderDetails();
        IOrderDetails details = actualOrder.getOrderDetails();

        if (details != null) {
            actualDetails.setPayload(details.getPayload());
            actualDetails.setImageUrl(details.getImageUrl());
        }

        actualOrder.setOrderDetails(actualDetails);

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

        logger.info("Order was saved: {}", actualOrder);
        return actualOrder;
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

        Order actualOrder = (Order) order;

        IOrderDetails details = order.getOrderDetails();
        if (details != null) {
            OrderDetails actualDetails = new OrderDetails();
            actualDetails.setIdOrder(details.getIdOrder());
            actualDetails.setImageUrl(details.getImageUrl());
            actualDetails.setPayload(details.getPayload());

            actualOrder.setOrderDetails(actualDetails);
        } else {
            OrderDetails actualDetails = new OrderDetails();

            actualOrder.setOrderDetails(actualDetails);
        }

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

        Order actualOrder = (Order) getByGuid(guid);

        try {
            orderRepository.deleteById(actualOrder.getIdOrder());
        } catch (PersistenceException e) {
            logger.error("Order wasn`t deleted from db", actualOrder);
            throw new OrderServiceException("Order wasn`t deleted: " + actualOrder);
        }

        logger.info("Order with guid: {}, was deleted", guid);
    }
}
