package com.softserve.itacademy.kek.services.tracking;

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
import com.softserve.itacademy.kek.services.IOrderEventService;

@Service
public class OrderPayloadObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderPayloadObserver.class);

    public final ApplicationEventPublisher eventPublisher;

    private IOrderEventService orderEventService;

    @Autowired
    public OrderPayloadObserver(IOrderEventService orderEventService,
                                ApplicationEventPublisher eventPublisher) {
        this.orderEventService = orderEventService;
        this.eventPublisher = eventPublisher;
    }


    @Scheduled(fixedRate = 6000)
    public void getPayloadsForDeliveringOrders() throws OrderEventServiceException {
        if (!OrderTrackingService.getActiveEmitters().isEmpty()) {
            OrderPayloadWrapper wrapper = new OrderPayloadWrapper();

            final List<IOrderEvent> lastEvents = orderEventService.findAllThatDeliveringNow();
            LOGGER.debug("Get last event for every order that is delivering now. Count of orders in delivering state = {}", lastEvents.size());

            final Function<OrderEvent, UUID> getOrderGuid = oe -> oe.getOrder().getGuid();
            final Map<UUID, String> ordersToPayloads = lastEvents
                    .stream()
                    .map(oe -> (OrderEvent) oe)
                    .collect(Collectors.toMap(getOrderGuid, OrderEvent::getPayload));
            wrapper.setMap(ordersToPayloads);

            this.eventPublisher.publishEvent(wrapper);
        }
    }
}
