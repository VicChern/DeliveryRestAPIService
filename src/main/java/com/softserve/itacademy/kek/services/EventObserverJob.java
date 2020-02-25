package com.softserve.itacademy.kek.services;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.OrderEventServiceException;
import com.softserve.itacademy.kek.exception.OrderServiceException;
import com.softserve.itacademy.kek.exception.UserServiceException;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEvent;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;

@Service
public class EventObserverJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(IOrderEventService.class);

    public final ApplicationEventPublisher eventPublisher;

    @Autowired
    private final OrderEventRepository orderEventRepository;

    @Autowired
    private final OrderRepository orderRepository;


    public EventObserverJob(OrderEventRepository orderEventRepository,
                                 OrderRepository orderRepository,
                                 ApplicationEventPublisher eventPublisher) {
        this.orderEventRepository = orderEventRepository;
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedRate = 1000)
    public void getLastEventByGuid(UUID guid) throws OrderEventServiceException {
        //TODO:: Change to find last event of TYPE STARTED for order guid{}
        OrderEvent orderEvent = orderEventRepository.findByGuid(guid);
        if (orderEvent == null) {
            LOGGER.warn("Event wasn't found in DB: guid = {}", guid);
            throw new OrderEventServiceException("Event wasn't found");
        }

        this.eventPublisher.publishEvent(orderEvent);
    }
}
