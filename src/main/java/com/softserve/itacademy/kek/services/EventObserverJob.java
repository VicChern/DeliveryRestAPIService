package com.softserve.itacademy.kek.services;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.Order;
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


    @Scheduled(fixedRate = 3000)
    public void getLastEventForStartedDeliverOrders() throws OrderEventServiceException {
        //TODO:: get last added events (from Db instead of stub) for every orderId that has type STARTED but does't have type DELIVERED

        //temporary added stub
        String json1 ="{\n" +
                "  \"location\": {\n" +
                "    \"lat\": 50.499247,\n" +
                "    \"lng\": 30.607360\n" +
                "  }\n" +
                "}";

        String json2 ="{\n" +
                "  \"location\": {\n" +
                "    \"lat\": new.Geo,\n" +
                "    \"lng\": new.Geo\n" +
                "  }\n" +
                "}";
        OrderEvent orderEvent1 = new OrderEvent();
        orderEvent1.setPayload(json1);
        orderEvent1.setGuid(UUID.randomUUID());
        Order order1 = new Order();
        order1.setGuid(UUID.fromString("2589d161-2912-4419-ab5d-2b4c09f9b73c"));
        order1.setIdOrder(1L);
        orderEvent1.setIdOrder(order1);

        OrderEvent orderEvent2 = new OrderEvent();
        orderEvent2.setPayload(json2);
        orderEvent2.setGuid(UUID.randomUUID());
        Order order2 = new Order();
        order2.setGuid(UUID.fromString("740d8f1c-8fbb-4328-933e-79640e15880b"));
        order2.setIdOrder(2L);
        orderEvent2.setIdOrder(order2);

        List<OrderEvent> orderEvents = new ArrayList<>();
        orderEvents.add(orderEvent1);
        orderEvents.add(orderEvent2);

        Function<OrderEvent, UUID> getOrderGuid = oe -> oe.getIdOrder().getGuid();
        Map<UUID, String> ordersToPayloads = orderEvents
                .stream()
                .collect(Collectors.toMap(getOrderGuid, OrderEvent::getPayload));

        MapWrapper wrapper = new MapWrapper();
        wrapper.setMap(ordersToPayloads);

        this.eventPublisher.publishEvent(wrapper);
    }
}
