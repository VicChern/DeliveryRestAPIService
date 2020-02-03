package com.softserve.itacademy.kek.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// how long to wait with opened connection?
// do we need fixed timeOut

@RestController
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

//    @GetMapping("/request")
//    @Async
//    public SseEmitter handleRequest() {
//        SseEmitter emitter = new SseEmitter();
//
//
//        ExecutorService service = Executors.newSingleThreadExecutor();
//        service.execute(() ->  {
//            try {
//                for ( int i = 0 ; isAlive ; i++ ) {
//                    SseEmitter.SseEventBuilder event = SseEmitter.event()
//                            .id(String.valueOf(i))
//                            .name("name")
//                            .data("message " + i, MediaType.TEXT_PLAIN);
//                    emitter.send(event);
//                    Thread.sleep(timeOut);
//                }
//            } catch (Exception e ) {
//                e.printStackTrace();
//                emitter.completeWithError(e);
//            }
//        });
//
//        return emitter;
//    }
//


    @GetMapping("/request")
    @Async
    public ResponseEntity<ResponseBodyEmitter> handleRbe() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        executor.execute(() -> {
            try {
                emitter.send(
                        "/request" + " @ " + new Date(), MediaType.TEXT_PLAIN);
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return new ResponseEntity(emitter, HttpStatus.OK);
    }


}

