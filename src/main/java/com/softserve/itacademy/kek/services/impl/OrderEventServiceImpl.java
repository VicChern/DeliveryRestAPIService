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

import com.softserve.itacademy.kek.exception.OrderEventServiceException;
import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.services.IOrderEventService;

/**
 * Service implementation for {@link IOrderEventService}
 */
@Service
public class OrderEventServiceImpl implements IOrderEventService {

    private final static Logger LOGGER = LoggerFactory.getLogger(IOrderEventService.class);

    private final OrderEventRepository orderEventRepository;
    private final OrderEventTypeRepository orderEventTypeRepository;
    private final OrderRepository orderRepository;
    private final ActorRepository actorRepository;

    @Autowired
    public OrderEventServiceImpl(OrderEventRepository orderEventRepository, OrderRepository orderRepository, ActorRepository actorRepository, OrderEventTypeRepository orderEventTypeRepository) {
        this.orderEventRepository = orderEventRepository;
        this.orderEventTypeRepository = orderEventTypeRepository;
        this.orderRepository = orderRepository;
        this.actorRepository = actorRepository;
    }

    @Transactional
    @Override
    public IOrderEvent create(IOrderEvent iOrderEvent, UUID orderGuid) throws OrderEventServiceException {
        LOGGER.info("Saving OrderEvent to db: {}", iOrderEvent);
        final OrderEvent orderEvent = new OrderEvent();

        final Order order = orderRepository.findByGuid(orderGuid);
        final Actor actor = actorRepository.findByGuid(iOrderEvent.getActor().getGuid());
        final OrderEventType orderEventType = orderEventTypeRepository.findByName(iOrderEvent.getOrderEventType().getName());


        orderEvent.setOrder(order);
        orderEvent.setGuid(iOrderEvent.getGuid());
        orderEvent.setActor(actor);
        orderEvent.setOrderEventType(orderEventType);
        orderEvent.setPayload(iOrderEvent.getPayload());

        try {
            orderEventRepository.save(orderEvent);
        } catch (PersistenceException e) {
            LOGGER.error("Order event wasn`t saved: {}", orderEvent);
            throw new OrderServiceException("Order event wasn`t saved");
        }

        LOGGER.info("Order event was saved: {}", orderEvent);
        return orderEvent;
    }

    @Transactional(readOnly = true)
    @Override
    public IOrderEvent getByGuid(UUID guid) throws OrderEventServiceException {
        LOGGER.info("Getting OrderEvent from db by guid");
        final OrderEvent orderEvent;

        try {
            orderEvent = orderEventRepository.findByGuid(guid);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Order event with guid: {}, wasn`t found", guid);
            throw new OrderServiceException("Order event with guid: " + guid + ", wasn`t found");
        }

        LOGGER.info("Order event with guid: {}, was found: {}", guid, orderEvent);
        return orderEvent;
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderEvent> getAllEventsForOrder(IOrder order) {
        LOGGER.info("Getting OrderEvent from db by Order");

        final List<OrderEvent> orderEventList;

        try {
            orderEventList = orderEventRepository.getOrderEventsByOrder(order);
        } catch (Exception e) {
            LOGGER.error("An error occurred: {}", e.toString());
            throw e;
        }

        return orderEventList;
    }
}
