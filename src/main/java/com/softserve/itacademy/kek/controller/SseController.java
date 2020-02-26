package com.softserve.itacademy.kek.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

//TODO:: Make this controller extends OrderController
@RestController
public class SseController {
    private static final Logger logger = LoggerFactory.getLogger(SseController.class);

    //TODO:: need to be concurrent?
    //This emitter's List represents connections from few clients (like laptop browsers, phone app etc.)
    //TODO:: Change emitters List to Map<OrderId, <List<SseEmitter>>> with orderId keys (map will represent all connections from different clients for different orderIds)
    private final List<SseEmitter> EMITTERS = new ArrayList<>();


    @GetMapping(value = "/orders/{id}/events/payload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> getLastEventPayload(@PathVariable String id) throws Exception{
        //TODO:: Add interceptor to log requests https://www.baeldung.com/spring-http-logging
        logger.info("Getting request to provide last event payload for order guid={}", id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Cache-Control", "no-store");

        //TODO:: if OrderId is not in emitter list than check in DB last order event type and date -> redirect
        // client to static endpoint with order info (Order status: delivered at 25.01.2019 14:00 )

        //keep connection open for 180 seconds, than browser will reconnect
        SseEmitter emitter = new SseEmitter(180_000L);

        EMITTERS.add(emitter);

        emitter.onCompletion(() -> EMITTERS.remove(emitter));
        emitter.onTimeout(() -> EMITTERS.remove(emitter));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(emitter);
    }


    @EventListener
    public void onLastEventPayload(String payload) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        //TODO:: delete emitters from Emitter list with orderId, that was not received from Publisher (then this orderId was delivered)
        EMITTERS.forEach(emitter -> {
            try {
                emitter.send(payload);
                logger.info("Send payload {} for order guid=", payload);
            }
            catch (IOException e) {
                deadEmitters.add(emitter);
                logger.debug("Can't send geolocation because emitter for order guid= was completed or closed by timeout");
            }
        });

        EMITTERS.removeAll(deadEmitters);
    }

}
