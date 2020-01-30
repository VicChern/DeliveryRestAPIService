package com.softserve.itacademy.kek.controller;


import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// how long to wait with opened connection?
// do we need fixed timeOut

@Controller
public class SseController {
    private int timeOut = 1000;
    private boolean isAlive = true;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public SseController() {
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
    public SseEmitter handleRequest() {
        SseEmitter emitter = new SseEmitter();


        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() ->  {
            try {
                for ( int i = 0 ; isAlive ; i++ ) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .id(String.valueOf(i))
                            .name("name")
                            .data("message " + i, MediaType.TEXT_PLAIN);
                    emitter.send(event);
                    Thread.sleep(timeOut);
                }
            } catch (Exception e ) {
                e.printStackTrace();
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}

