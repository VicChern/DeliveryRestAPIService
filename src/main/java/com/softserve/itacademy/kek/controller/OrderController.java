package com.softserve.itacademy.kek.controller;

import org.apache.log4j.Logger;
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
@RequestMapping(path = "/orders")
public class OrderController extends DefaultController {

    final Logger logger = Logger.getLogger(UserController.class);

    // Build Response (stub, temporary method)
    private String getJSON(String id, String status) {
        JSONObject json = new JSONObject();
        json.put("orderID", id);
        json.put("status", status);
        return json.toString();
    }

    /**
     * Get information about orders
     *
     * @return list of order objects as a JSON
     */
    @GetMapping
    public ResponseEntity<String> getOrderList() {
        logger.info("Client requested the list of all orders");

        JSONObject json = new JSONObject();
        json.append("orderID", "1").append("orderID", "2").append("orderID", "3");
        json.put("status", "received");
        return ResponseEntity.ok(json.toString());
    }

    /**
     * Creates a new order
     *
     * @param body order object as a JSON
     * @return created order object as a JSON
     */
    @PostMapping
    public ResponseEntity<String> addOrder(@RequestBody String body) {
        logger.info("Sending the created order to the client");

        return ResponseEntity.ok(body);
    }

    /**
     * Returns information about the requested order
     *
     * @param id order ID from the URN
     * @return order object as a JSON
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getOrder(@PathVariable String id) {
        logger.info("Sending the specific order (" + id + ") to the client");

        return ResponseEntity.ok(getJSON(id, "received"));
    }

    /**
     * Modifies information of the specified order
     *
     * @param id   order ID from the URN
     * @param body order object as a JSON
     * @return modified order object as a JSON
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifyOrder(@PathVariable String id, @RequestBody String body) {
        logger.info("Sending the modified order to the client");

        return ResponseEntity.ok(body);
    }

    /**
     * Removes the specified order
     *
     * @param id order ID from the URN
     * @return operation status as a JSON
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable String id) {
        logger.info("Order (" + id + ") successfully deleted");

        return ResponseEntity.ok(getJSON(id, "deleted"));
    }

    /**
     * Finds events of the specific order
     *
     * @param id order ID from the URN
     * @return list of the event objects as a JSON
     */
    @GetMapping("/{id}/events")
    public ResponseEntity<String> getEvents(@PathVariable String id) {
        logger.info("Sending the list of order (" + id + ") events to the client");

        return ResponseEntity.ok(getJSON(id, "received"));
    }

    /**
     * Adds a new event for the specific order
     *
     * @param id   order ID from the URN
     * @param body order object as a JSON
     * @return created event objects as a JSON
     */
    @PostMapping("/{id}/events")
    public ResponseEntity<String> addEvent(@PathVariable String id, @RequestBody String body) {
        logger.info("Sending the created order(" + id + ") events to the client");

        return ResponseEntity.ok(body);
    }
}
