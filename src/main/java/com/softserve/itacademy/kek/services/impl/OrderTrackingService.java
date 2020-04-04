package com.softserve.itacademy.kek.services.impl;

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
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.softserve.itacademy.kek.services.OrderTrackingWrapper;

@Service
public class OrderTrackingService {
    private static final Logger logger = LoggerFactory.getLogger(OrderTrackingService.class);

    //map represents all connections from different clients for particular orderId
    private static final Map<UUID, List<SseEmitter>> ACTIVE_EMITTERS = new ConcurrentHashMap<>();

    @EventListener
    public void enrichEmitters(OrderTrackingWrapper eventWrapper) {
        final Map<UUID, List<SseEmitter>> deadEmitters = new HashMap<>();
        final Map<UUID, String> deliveringOrdersToPayloads = eventWrapper.getMap();

        //get order's guids for which delivering was finished
        List<UUID> alreadyDelivered = ACTIVE_EMITTERS
                .keySet()
                .stream()
                .filter(key -> !(deliveringOrdersToPayloads.containsKey(key)))
                .collect(Collectors.toList());

        //complete emitters for which order's delivering was finished
        ACTIVE_EMITTERS.forEach((k, v) -> {
            if (alreadyDelivered.contains(k)) {
                v.forEach(ResponseBodyEmitter::complete);
            }
        });

        //remove emitters for which order's delivering was finished
        ACTIVE_EMITTERS.keySet().removeAll(alreadyDelivered);

        //update payloads for emitters of Orders that are delivering
        ACTIVE_EMITTERS.forEach((guid, emitters) -> {
            emitters.forEach(sseEmitter -> {
                String payload = deliveringOrdersToPayloads.get(guid);
                try {
                    sseEmitter.send(formatForApacheClient(payload));
                    logger.debug("Send payload {} for order guid={}", payload, guid);
                } catch (IOException e) {

                    //add broken emitters into separate Map
                    addEmitter(deadEmitters, guid, sseEmitter);
                    logger.debug("All emitters for order guid={} was completed or closed by timeout. Server can't send geolocation", guid);
                }
            });
        });

        //remove emitters, that were closed by timeout or completed
        deadEmitters.forEach((key, list) -> ACTIVE_EMITTERS.get(key).removeAll(list));
    }

    public void addEmitter(Map<UUID, List<SseEmitter>> map, UUID guid, SseEmitter emitter) {
        List<SseEmitter> emitterWrappedInList = new ArrayList<>();
        emitterWrappedInList.add(emitter);
        if (map.containsKey(guid)) {
            map.get(guid).add(emitter);
        } else {
            map.put(guid, emitterWrappedInList);
        }
    }

    //needed because Apache Client don't recognize events without space "data: event"
    public String formatForApacheClient(String text) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        } else {
            return " " + text;
        }
    }

    public static Map<UUID, List<SseEmitter>> getActiveEmitters() {
        return ACTIVE_EMITTERS;
    }
}
