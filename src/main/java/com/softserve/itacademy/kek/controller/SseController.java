package com.softserve.itacademy.kek.controller;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// TODO: Add logger

@RestController
@Async
public class SseController {
    private int delay = 5000;
    private long sessionTimeout;
    private boolean isConnected = true;

    public SseController() {
        this.sessionTimeout = 180_000;
    }

    public SseController(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public SseController(long sessionTimeout, int delay) {
        this.sessionTimeout = sessionTimeout;
        this.delay = delay;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public int getDelay() {
        return delay;
    }

    /**
     * Creating emitter for 3 minutes(can be changed) and pushing message every 5 seconds
     * data sending "message" for now, should be changed for actual coordinates
     *
     * @return
     */

    @GetMapping(value = "/request", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Async
    public SseEmitter handleRequest() {
        SseEmitter emitter = new SseEmitter(sessionTimeout);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            try {
                for (; isConnected; ) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data("message", MediaType.TEXT_EVENT_STREAM);
                    emitter.send(event);
                    Thread.sleep(delay);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }
}

