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

    //TODO:: Change method naming
    @Scheduled(fixedRate = 5000)
    public void getLastEventForStartedDeliverOrders() throws OrderEventServiceException {
        //TODO:: get last added events for every orderId that has type STARTED but does't have type DELIVERED

        //stub
        String payload ="{\n" +
                "  \"location\": {\n" +
                "    \"lat\": 50.499247,\n" +
                "    \"lng\": 30.607360\n" +
                "  }\n" +
                "}";

        //TODO:: publish {OrderId, geolocation} list or object OR publish List<OrderEvents>
        this.eventPublisher.publishEvent(payload);
    }
}
