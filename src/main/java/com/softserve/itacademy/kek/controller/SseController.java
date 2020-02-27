package com.softserve.itacademy.kek.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.softserve.itacademy.kek.services.MapWrapper;


//TODO:: Make this controller extends OrderController
@RestController
public class SseController {
    private static final Logger logger = LoggerFactory.getLogger(SseController.class);

    //TODO:: need to be concurrent?
    //map represents all connections from different clients for particular orderId
    private Map<UUID, List<SseEmitter>> ORDER_EMITTERS = new Hashtable<>();

    @GetMapping(value = "/orders/{guid}/events/messaging", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> getLastEventPayload(@PathVariable String guid) {
        //TODO:: Add interceptor to log requests https://www.baeldung.com/spring-http-logging
        logger.info("Getting request to provide last event payload for order guid={}", guid);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Cache-Control", "no-store");

        //TODO:: check in DB IF this orderId has STARTED and not DELIVERED yet
        // if not started or was already delivered -> return response
        // (Order status: delivered at 25.01.2019 14:00 )

        //keep connection open for 180 seconds, than browser will reconnect
        SseEmitter emitter = new SseEmitter(180_000L);

        //TODO:: Rewrite in java 8
        UUID uuid = UUID.fromString(guid);
        List<SseEmitter> emitterWrapedInList = new ArrayList<>();
        emitterWrapedInList.add(emitter);
        if(!ORDER_EMITTERS.containsKey(uuid)) {
            ORDER_EMITTERS.put(uuid, emitterWrapedInList);
        }
        else {
            ORDER_EMITTERS.get(uuid).add(emitter);
        }

        emitter.onCompletion(() -> ORDER_EMITTERS.get(uuid).remove(emitter));
        emitter.onTimeout(() -> ORDER_EMITTERS.get(uuid).remove(emitter));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(emitter);
    }


    //TODO:: Remove MapWrapper and use Spring Events properly that listener will accept generics List<UUID, OrderEvent>
    @EventListener
    public void onLastEventPayload(MapWrapper eventWrapper) {
        Map<UUID, List<SseEmitter>> deadEmitters = new HashMap<>();
        Map<UUID, String> ordersToPayloads = eventWrapper.getMap();

        //remove emitters for which order delivering was completed already
//        Collection<UUID> deliveringOrderIds = ordersToPayloads
//                .keySet()
//                .stream()
//                .map(IOrder::getGuid)
//                .collect(toList());
//
//        ORDER_EMITTERS.keySet().removeIf(key -> !(deliveringOrderIds.contains(key)));

        //update payloads for emitters of Orders that are delivering
        ORDER_EMITTERS.forEach((guid, emitters) -> {
            emitters.forEach(sseEmitter -> {
                String payload = ordersToPayloads.get(guid);
                try{
                    sseEmitter.send(payload);
                    logger.info("Send payload {} for order guid={}", payload, guid);
                } catch (IOException e){

                    //TODO:: Rewrite in java 8
                    List<SseEmitter> emitterWrapedInList = new ArrayList<>();
                    emitterWrapedInList.add(sseEmitter);
                    if(deadEmitters.containsKey(guid)) {
                        deadEmitters.get(guid).add(sseEmitter);
                    }
                    else {
                        deadEmitters.put(guid, emitterWrapedInList);
                        logger.info("All emitters for order guid={} was completed or closed by timeout. Server can't send geolocation", guid);
                    }

                }
            });
        });

        //remove emitters, that were closed by timeout or completed
        deadEmitters.forEach((key, list) -> ORDER_EMITTERS.get(key).removeAll(list));
    }

}
