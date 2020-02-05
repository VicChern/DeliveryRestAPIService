package com.softserve.itacademy.kek.controller;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
public class SseController {
    private int timeOut = 1000;
    private boolean isConnected = true;
    private SseEmitter emitter;
    private ExecutorService service;

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int seconds) {

        if ( timeOut < 0 ) {
            this.timeOut = 0;
        } else  {
            this.timeOut = seconds * 1000;
        }
    }

    @GetMapping("/request")
    @Async
    public SseEmitter handleRequest(String message) {
        emitter = new SseEmitter();
        service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            try {
                for ( ; isConnected; ) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data("message ", MediaType.TEXT_PLAIN);
                    emitter.send(event);
                    Thread.sleep(timeOut);
                }
            } catch (Exception e) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
