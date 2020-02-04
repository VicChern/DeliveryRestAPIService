package com.softserve.itacademy.kek.controller;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users", produces = "application/json; charset=UTF-8")
public class OrderController extends DefaultController {

    // Build Response (stub, temporary method)
    private String getJSON(String id, String status) {
        JSONObject json = new JSONObject();
        json.put("orderID", id);
        json.put("status", status);
        return json.toString();
    }

    @GetMapping
    public ResponseEntity<String> getOrderList() {
        JSONObject json = new JSONObject();
        json.append("OrderID", "1").append("OrderID", "2").append("OrderID", "3");
        json.put("status", "received");
        return ResponseEntity.ok(json.toString());
    }

    @PostMapping
    public ResponseEntity<String> addOrder(@RequestBody String body) {
        return ResponseEntity.ok(getJSON("new", "added"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(getJSON(id, "received"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifyOrder(@PathVariable String id, @RequestBody String body) {
        return ResponseEntity.ok(getJSON(id, "modified"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable String id) {
        return ResponseEntity.ok(getJSON(id, "deleted"));
    }
    
    @GetMapping("/{id}/events")
    public ResponseEntity<String> getEvents(@PathVariable String id) {
        return ResponseEntity.ok(getJSON(id, "Created"));
    }

    @PostMapping("/{id}/events")
    public ResponseEntity<String> addEvent(@PathVariable String id, @RequestBody String body) {
        return ResponseEntity.ok(getJSON("new", "added"));
    }
}
