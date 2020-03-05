package com.softserve.itacademy.kek.services;

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

import com.softserve.itacademy.kek.exception.OrderEventServiceException;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEvent;

@Service
public class EventObserverJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventObserverJob.class);

    public final ApplicationEventPublisher eventPublisher;

    private IOrderEventService orderEventService;

    @Autowired
    public EventObserverJob(IOrderEventService orderEventService,
                            ApplicationEventPublisher eventPublisher) {
        this.orderEventService = orderEventService;
        this.eventPublisher = eventPublisher;
    }


    @Scheduled(fixedRate = 15000)
    public void getPayloadsForDeliveringOrders() throws OrderEventServiceException {
       final List<IOrderEvent> lastEvents = orderEventService.findAllThatDeliveringNow();
       LOGGER.debug("Get last event for every order that is delivering now. Count of orders in delivering state = {}", lastEvents.size());

       final Function<OrderEvent, UUID> getOrderGuid = oe -> oe.getOrder().getGuid();
       final Map<UUID, String> ordersToPayloads = lastEvents
                .stream()
                .map(oe -> (OrderEvent) oe)
                .collect(Collectors.toMap(getOrderGuid, OrderEvent::getPayload));

       OrderTrackingWrapper wrapper = new OrderTrackingWrapper();
       wrapper.setMap(ordersToPayloads);

       this.eventPublisher.publishEvent(wrapper);
    }
}
