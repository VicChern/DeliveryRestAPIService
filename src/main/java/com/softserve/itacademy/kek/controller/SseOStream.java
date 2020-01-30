package com.softserve.itacademy.kek.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Date;

@Controller
@Async
public class SseOStream {

    @GetMapping("/request2")
    public ResponseEntity<StreamingResponseBody> handleRba() {
        StreamingResponseBody stream = out -> {
            String msg = "ghf" + " @ " + new Date();
            out.write(msg.getBytes());
        };
        return new ResponseEntity(stream, HttpStatus.OK);
    }
}
