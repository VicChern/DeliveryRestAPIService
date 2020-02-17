package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.exception.OrderEventServiceException;
import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.modelInterfaces.IOrderEvent;
import com.softserve.itacademy.kek.models.OrderEvent;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.services.IOrderEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.UUID;

/**
 * Service implementation for {@link IOrderEventService}
 */
@Service
public class OrderEventServiceImpl implements IOrderEventService {

    final Logger logger = LoggerFactory.getLogger(IOrderEventService.class);

    private final OrderEventRepository orderEventRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderEventServiceImpl(OrderEventRepository orderEventRepository, OrderRepository orderRepository) {
        this.orderEventRepository = orderEventRepository;
        this.orderRepository = orderRepository;
    }


    @Transactional
    @Override
    public IOrderEvent save(IOrderEvent orderEvent) throws OrderEventServiceException {
        logger.info("Saving OrderEvent to db: {}", orderEvent);
        UUID orderGuid = orderEvent.getIdOrder().getGuid();

        try {
            orderRepository.findByGuid(orderGuid);
        } catch (EntityNotFoundException e) {
            logger.error("There is no order for order event with order guid: {}", orderGuid);
            throw new OrderServiceException("There is no order for order event with order guid: " + orderGuid);
        }

        try {
            orderEventRepository.save((OrderEvent) orderEvent);
        } catch (PersistenceException e) {
            logger.error("Order event wasn`t saved: {}", orderEvent);
            throw new OrderServiceException("Order event wasn`t saved");
        }

        logger.info("Order event was saved: {}", orderEvent);
        return orderEvent;
    }

    @Transactional(readOnly = true)
    @Override
    public IOrderEvent getByGuid(UUID guid) throws OrderEventServiceException {
        logger.info("Getting OrderEvent from db by guid");
        OrderEvent orderEvent;

        try {
            orderEvent = orderEventRepository.findByGuid(guid);
        } catch (EntityNotFoundException e) {
            logger.error("Order event with guid: {}, wasn`t found", guid);
            throw new OrderServiceException("Order event with guid: " + guid + ", wasn`t found");
        }

        logger.info("Order event with guid: {}, was found: {}", guid, orderEvent);
        return orderEvent;
    }
}
