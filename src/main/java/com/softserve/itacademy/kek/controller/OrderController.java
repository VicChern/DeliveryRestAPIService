package com.softserve.itacademy.kek.controller;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.dto.OrderDetailsDto;
import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.dto.OrderEventDto;
import com.softserve.itacademy.kek.dto.OrderEventTypesDto;


@RestController
@RequestMapping(path = "/orders")
public class OrderController extends DefaultController {

    final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final Gson gson = new Gson();

    /**
     * Temporary method for OrderDto stub
     *
     * @return {@link OrderDto} stub
     */
    private OrderDto getOrderDtoStub() {
        OrderDetailsDto orderDetails = new OrderDetailsDto("some info", "https://mypicture");
        OrderDto order = new OrderDto("MyTenant", "user123", "123",
                "summary", orderDetails);
        return order;
    }

    /**
     * Temporary method for OrderEventDto stub
     *
     * @return {@link OrderEventDto} stub
     */
    private OrderEventDto getOrderEventStub() {
        OrderEventDto event = new OrderEventDto("wqewqe1r1", "123",
                "some info", OrderEventTypesDto.DELIVERED);
        return event;
    }

    /**
     * Get information about orders
     *
     * @return Response Entity with list of {@link OrderDto} objects
     */
    @GetMapping(produces = "application/vnd.softserve.order+json")
    public ResponseEntity<List<OrderDto>> getOrderList() {
        logger.info("Client requested the list of all orders");

        List<OrderDto> orderList = new ArrayList<>();
        orderList.add(getOrderDtoStub());

        logger.info("Sending list of all orders to the client:\n{}", gson.toJson(orderList));
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    /**
     * Creates a new order
     *
     * @param order DTO object as a JSON
     * @return created {@link OrderDto} object
     */
    @PostMapping
    public ResponseEntity<OrderDto> addOrder(@RequestBody OrderDto order) {
        logger.info("Order has been sent:\n{}", gson.toJson(order));
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    /**
     * Returns information about the requested order
     *
     * @param id order ID from the URN
     * @return Response Entity with {@link OrderDto} object
     */
    @GetMapping(value = "/{id}", produces = "application/vnd.softserve.order+json")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long id) {
        OrderDto order = getOrderDtoStub();

        logger.info("Sending the specific order ({}) to the client:\n{}", id, gson.toJson(order));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * Modifies information of the specified order
     *
     * @param id    order ID from the URN
     * @param order order object as a JSON
     * @return Response entity with modified {@link OrderDto} object
     */
    @PutMapping(value = "/{id}", consumes = "application/vnd.softserve.order+json",
            produces = "application/vnd.softserve.order+json")
    public ResponseEntity<OrderDto> modifyOrder(@PathVariable String id, @RequestBody OrderDto order) {
        logger.info("Sending the modified order({}) to the client", id);
        logger.info("Order was modified:\n{}", gson.toJson(order));
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    /**
     * Removes the specified order
     *
     * @param id order ID from the URN
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrder(@PathVariable String id) {
        logger.info("Order ({}) successfully deleted", id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Finds events of the specific order
     *
     * @param id order ID from the URN
     * @return list of the {@link OrderEventDto} objects
     */
    @GetMapping(value = "/{id}/events", produces = "application/vnd.softserve.event+json")
    public ResponseEntity<List<OrderEventDto>> getEvents(@PathVariable String id) {
        logger.info("Sending the list of order ({}) events to the client", id);

        List<OrderEventDto> orders = new ArrayList<>();
        orders.add(getOrderEventStub());

        logger.info("Sending the list: {}", gson.toJson(orders));
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Adds a new event for the specific order
     *
     * @param id   order ID from the URN
     * @param body {@link OrderEventDto} object
     * @return Response Entity with created {@link OrderEventDto} objects
     */
    @PostMapping(value = "/{id}/events", consumes = "application/vnd.softserve.event+json",
            produces = "application/vnd.softserve.event+json")
    @ResponseStatus()
    public ResponseEntity<OrderEventDto> addEvent(@PathVariable String id, @RequestBody OrderEventDto body) {
        logger.info("Sending the created order({}) events to the client", id);
        logger.info("Event have been added: {}", gson.toJson(body));
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }
}
