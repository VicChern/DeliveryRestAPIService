package com.softserve.itacademy.kek.services.tracking;

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

@Service
public class OrderTrackingService {
    private static final Logger logger = LoggerFactory.getLogger(OrderTrackingService.class);

    //map represents all connections from different clients for particular orderId
    private static final Map<UUID, List<SseEmitter>> ACTIVE_EMITTERS = new ConcurrentHashMap<>();

    public static Map<UUID, List<SseEmitter>> getActiveEmitters() {
        return ACTIVE_EMITTERS;
    }

    @EventListener
    public void enrichEmitters(OrderPayloadWrapper eventWrapper) {
        final Map<UUID, List<SseEmitter>> deadEmitters = new HashMap<>();
        final Map<UUID, String> deliveringOrdersAndPayloads = eventWrapper.getMap();
        logger.trace("Count of orders that are delivering is {}", deliveringOrdersAndPayloads.size());

        //get order's guids for which delivering was finished
        List<UUID> alreadyDelivered = ACTIVE_EMITTERS
                .keySet()
                .stream()
                .filter(key -> !(deliveringOrdersAndPayloads.containsKey(key)))
                .collect(Collectors.toList());
        logger.trace("Count of orders that are not valid for tracking anymore is {}", alreadyDelivered.size());

        //complete emitters for which order's delivering was finished
        ACTIVE_EMITTERS.forEach((k, v) -> {
            if (alreadyDelivered.contains(k)) {
                v.forEach(ResponseBodyEmitter::complete);
            }
        });
        logger.trace("Completed emitters for which delivering was already finished");

        //remove emitters for which order's delivering was finished
        ACTIVE_EMITTERS.keySet().removeAll(alreadyDelivered);
        logger.debug("Removed emitters from tracking map for which delivering was finished");

        //update payloads for emitters of Orders that are delivering
        ACTIVE_EMITTERS.forEach((guid, emitters) -> {
            emitters.forEach(sseEmitter -> {
                String payload = deliveringOrdersAndPayloads.get(guid);
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
        ACTIVE_EMITTERS.values().removeIf(List::isEmpty);
        logger.debug("Removed all emitters from tracking map that were closed by timeout or completion");
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
}
