package com.softserve.itacademy.kek.controller;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.softserve.itacademy.kek.controller.utils.KekMappingValues;
import com.softserve.itacademy.kek.exception.TrackingException;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.enums.EventTypeEnum;
import com.softserve.itacademy.kek.services.IOrderEventService;
import com.softserve.itacademy.kek.services.tracking.OrderTrackingService;

@RestController
public class SseController {
    private static final Logger logger = LoggerFactory.getLogger(SseController.class);
    public static final long TIMEOUT = 180_000L;

    @Autowired
    OrderTrackingService trackingService;
    @Autowired
    private IOrderEventService orderEventService;

    @GetMapping(value = KekMappingValues.ORDERS_GUID_TRACKING)
    public ResponseEntity<SseEmitter> trackOrder(@PathVariable final UUID orderGuid) {
        logger.info("Getting request to provide last event payload for order guid={}", orderGuid);

        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.CACHE_CONTROL, "no-store");

        final IOrderEvent lastAddedEvent = orderEventService.getLastAddedEvent(orderGuid);

        if (!hasStartedType(lastAddedEvent)) {
            throw new TrackingException(String.format("Order %s is not delivering now", orderGuid));
        }

        final SseEmitter emitter = new SseEmitter(TIMEOUT);

        try {
            sendPayload(orderGuid, lastAddedEvent, emitter);
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


    private void sendPayload(final UUID orderGuid,
                             final IOrderEvent lastAddedEvent,
                             final SseEmitter emitter) throws IOException {
        final String payload = lastAddedEvent.getPayload();
        emitter.send(trackingService.formatForApacheClient(payload));
        logger.debug("Send payload {} for order guid={}", payload, orderGuid);
    }

    private boolean hasStartedType(IOrderEvent lastAddedEvent) {
        String type = lastAddedEvent.getOrderEventType().getName();
        return type.equals(EventTypeEnum.STARTED.toString());
    }
}
