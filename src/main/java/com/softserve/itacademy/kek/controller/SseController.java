package com.softserve.itacademy.kek.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.softserve.itacademy.kek.controller.utils.KekMappingValues;
import com.softserve.itacademy.kek.exception.TrackingException;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.enums.EventTypeEnum;
import com.softserve.itacademy.kek.services.IOrderEventService;
import com.softserve.itacademy.kek.services.impl.OrderTrackingService;

@RestController
public class SseController {
    private static final Logger logger = LoggerFactory.getLogger(SseController.class);

    @Autowired
    private IOrderEventService orderEventService;

    @Autowired
    OrderTrackingService trackingService;

    @GetMapping(value = KekMappingValues.ORDERS_GUID_TRACKING)
    public ResponseEntity<SseEmitter> trackOrder(@PathVariable final UUID orderGuid) {
        logger.info("Getting request to provide last event payload for order guid={}", orderGuid);

        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.CACHE_CONTROL, "no-store");

        final IOrderEvent lastAddedEvent = orderEventService.getLastAddedEvent(orderGuid);

        if (!hasStartedType(lastAddedEvent)) {
            throw new TrackingException(String.format("Order %s is not delivering now", orderGuid));
        }

        final SseEmitter emitter = new SseEmitter(180_000L);

        try {
            final String payload = lastAddedEvent.getPayload();
            emitter.send(trackingService.formatForApacheClient(payload));
            logger.debug("Send payload {} for order guid={}", payload, orderGuid);

            trackingService.addEmitter(OrderTrackingService.getActiveEmitters(), orderGuid, emitter);
            logger.debug("Emitter for order guid={} was added for tracking", orderGuid);

        } catch (IOException e) {
            logger.debug("Emitter was closed by timeout. Server can't send geolocation");

        } catch (NullPointerException e) {
            logger.debug("Tracking for order guid={} was not started", orderGuid);

        }

        emitter.onCompletion(() -> OrderTrackingService.getActiveEmitters().get(orderGuid).remove(emitter));
        logger.trace("One emitter for order guid={} was completed", orderGuid);

        emitter.onTimeout(() -> OrderTrackingService.getActiveEmitters().get(orderGuid).remove(emitter));
        logger.trace("One emitter for order guid={} was timeouted", orderGuid);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(emitter);
    }

    private boolean hasStartedType(IOrderEvent lastAddedEvent) {
        String type = lastAddedEvent.getOrderEventType().getName();
        return type.equals(EventTypeEnum.STARTED.toString());
    }
}
