package com.softserve.itacademy.kek.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.softserve.itacademy.kek.exception.TrackingException;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.enums.EventType;
import com.softserve.itacademy.kek.services.IOrderEventService;
import com.softserve.itacademy.kek.services.OrderTrackingWrapper;

@RestController
public class SseController {

    @Autowired
    IOrderEventService orderEventService;

    private static final Logger logger = LoggerFactory.getLogger(SseController.class);

    //map represents all connections from different clients for particular orderId
    private static final Map<UUID, List<SseEmitter>> ORDER_EMITTERS = new Hashtable<>();

    @GetMapping(value = "/orders/{guid}/events/messaging")
    public ResponseEntity<SseEmitter> trackOrder(@PathVariable final UUID guid) {
        logger.info("Getting request to provide last event payload for order guid={}", guid);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Cache-Control", "no-store");

        IOrderEvent lastAddedEvent = orderEventService.getLastAddedEvent(guid);

        if (!isDelivering(lastAddedEvent)) {
            throw new TrackingException(String.format("Order %s is not delivering now", guid));
        }

        SseEmitter emitter = new SseEmitter(180_000L);
        String payload = lastAddedEvent.getPayload();
        try {
            emitter.send(payload);
            logger.debug("Send payload {} for order guid={}", payload, guid);
            List<SseEmitter> emitterWrappedInList = new ArrayList<>();
            emitterWrappedInList.add(emitter);
            if (ORDER_EMITTERS.containsKey(guid)) {
                ORDER_EMITTERS.get(guid).add(emitter);
            } else {
                ORDER_EMITTERS.put(guid, emitterWrappedInList);
            }
        } catch (IOException e) {
            logger.debug("Emitter was closed by timeout. Server can't send geolocation");
        } catch (NullPointerException e) {
            logger.debug("Tracking for order guid={} was not started", guid);
        }

        //TODO:: we don't complete emitters, just delete them. Investigate could it cause problems
        //"Finally emitter.complete() is called to mark that request processing is complete so that the thread
        // responsible for sending the response can complete the request and be freed up for the next response to handle."
        emitter.onCompletion(() -> ORDER_EMITTERS.get(guid).remove(emitter));
        emitter.onTimeout(() -> ORDER_EMITTERS.get(guid).remove(emitter));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(emitter);
    }

    private boolean isDelivering(IOrderEvent lastAddedEvent) {
        String type = lastAddedEvent.getOrderEventType().getName();
        return type.equals(EventType.STARTED.toString());
    }


    //TODO:: Remove MapWrapper and use Spring Events properly that listener will accept generics List<UUID, OrderEvent>
    @EventListener
    public void enrichEmitters(OrderTrackingWrapper eventWrapper) {
        final Map<UUID, List<SseEmitter>> deadEmitters = new HashMap<>();
        final Map<UUID, String> deliveringOrdersToPayloads = eventWrapper.getMap();

        //remove emitters for which order delivering was completed already
        List<UUID> alreadyDelivered = ORDER_EMITTERS
                .keySet()
                .stream()
                .filter(key -> !(deliveringOrdersToPayloads.containsKey(key)))
                .collect(Collectors.toList());
        ORDER_EMITTERS.keySet().removeAll(alreadyDelivered);


        //update payloads for emitters of Orders that are delivering
        ORDER_EMITTERS.forEach((guid, emitters) -> {
            emitters.forEach(sseEmitter -> {
                String payload = deliveringOrdersToPayloads.get(guid);
                try {
                    sseEmitter.send(payload);
                    logger.debug("Send payload {} for order guid={}", payload, guid);
                } catch (IOException e) {
                    List<SseEmitter> emitterWrapedInList = new ArrayList<>();
                    emitterWrapedInList.add(sseEmitter);
                    if (deadEmitters.containsKey(guid)) {
                        deadEmitters.get(guid).add(sseEmitter);
                    } else {
                        deadEmitters.put(guid, emitterWrapedInList);
                        logger.debug("All emitters for order guid={} was completed or closed by timeout. Server can't send geolocation", guid);
                    }
                }
            });
        });

        //remove emitters, that were closed by timeout or completed
        deadEmitters.forEach((key, list) -> ORDER_EMITTERS.get(key).removeAll(list));
    }

}
