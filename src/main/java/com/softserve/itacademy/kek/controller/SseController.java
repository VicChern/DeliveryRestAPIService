package com.softserve.itacademy.kek.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    //TODO:: Change emitters List to Map with orderId keys (map will represent all connections from different clients for different orderIds)
    private final List<SseEmitter> EMITTERS = new ArrayList<>();


    @GetMapping(value = "/orders/{id}/events/payload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> getLastEventPayload(@PathVariable String id) throws Exception{
        //TODO:: Add interceptor to log requests https://www.baeldung.com/spring-http-logging
        logger.info("Getting request to provide last event payload for order guid={}", id);

        //stub
        String payload = "50.499247, 30.607360";

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Cache-Control", "no-store");

        //keep connection open for 180 seconds, than browser will reconnect
        SseEmitter emitter = new SseEmitter(180_000L);
        try {
            emitter.send(payload);
            logger.info("Send payload {} for order guid={}", payload, id);
        }
        catch (IOException e) {
            throw new Exception("Can't send geolocation because emitter for order guid=" + id
                    + "was completed or closed by timeout", e);
        }

        EMITTERS.add(emitter);

        emitter.onCompletion(() -> EMITTERS.remove(emitter));
        emitter.onTimeout(() -> EMITTERS.remove(emitter));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(emitter);
    }


//    @EventListener
//    public void onLastEventPayload(String payload) {
//        List<SseEmitter> deadEmitters = new ArrayList<>();
//        EMITTERS.forEach(emitter -> {
//            try {
//                emitter.send(payload);
//            }
//            catch (IOException e) {
//                deadEmitters.add(emitter);
//            }
//        });
//
//        EMITTERS.removeAll(deadEmitters);
//    }

}
