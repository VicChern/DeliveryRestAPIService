package com.softserve.itacademy.kek.controller;

import com.softserve.itacademy.kek.dto.OrderDetailsDto;
import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.dto.OrderEventDto;
import com.softserve.itacademy.kek.dto.OrderEventListDto;
import com.softserve.itacademy.kek.dto.OrderEventTypesDto;
import com.softserve.itacademy.kek.dto.OrderListDto;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.services.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequestMapping(path = "/orders")
public class OrderController extends DefaultController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final IOrderService orderService;

    @Autowired
    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Temporary method for OrderDto stub
     *
     * @return {@link OrderDto} stub
     */
    private OrderDto getOrderDtoStub() {
        OrderDetailsDto orderDetails = new OrderDetailsDto("some info", "https://mypicture");
        OrderDto order = new OrderDto(null, UUID.randomUUID(),
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
     * @return Response Entity with list of {@link OrderListDto} objects
     */
    @GetMapping(produces = "application/vnd.softserve.order+json")
    public ResponseEntity<OrderListDto> getOrderList() {
        logger.info("Client requested the list of all orders");

        OrderListDto orderList = new OrderListDto();
        orderList.addOrder(getOrderDtoStub());

        logger.info("Sending list of all orders to the client:\n{}", orderList);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderList);
    }

    /**
     * Creates a new order
     *
     * @param orders object as a JSON
     * @return created {@link OrderListDto} object
     */
    @PostMapping(value = "/{customerGuid}", consumes = "application/vnd.softserve.orderList+json", produces = "application/vnd.softserve.orderList+json")
    public ResponseEntity<OrderListDto> addOrder(@RequestBody @Valid OrderListDto orders, @PathVariable String customerGuid) {
        logger.info("Orders has been accepted:\n{}", orders);

        IOrder iOrder = orderService.create(orders.getOrderList().get(0), UUID.fromString(customerGuid));

        OrderDto orderDto = transform(iOrder);

        logger.info("Sending the created orders to the client:\n{}", orders);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new OrderListDto().addOrder(orderDto));
    }

    private OrderDto transform(IOrder iOrder) {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto(iOrder.getOrderDetails().getPayload(), iOrder.getOrderDetails().getImageUrl());
        OrderDto orderDto = new OrderDto(iOrder.getGuid(), iOrder.getTenant().getGuid(), iOrder.getSummary(), orderDetailsDto);
        return orderDto;
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

        logger.info("Sending the specific order ({}) to the client:\n{}", id, order);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(order);
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
    public ResponseEntity<OrderDto> modifyOrder(@PathVariable String id, @RequestBody @Valid OrderDto order) {
        logger.info("Sending the modified order({}) to the client", id);

        logger.info("Order was modified:\n{}", order);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(order);
    }

    /**
     * Removes the specified order
     *
     * @param id order ID from the URN
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrder(@PathVariable String id) {
        logger.info("Order ({}) successfully deleted", id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * Finds events of the specific order
     *
     * @param id order ID from the URN
     * @return list of the {@link OrderEventListDto} objects
     */
    @GetMapping(value = "/{id}/events", produces = "application/vnd.softserve.event+json")
    public ResponseEntity<OrderEventListDto> getEvents(@PathVariable String id) {
        OrderEventListDto orderEventList = new OrderEventListDto(id);
        orderEventList.addOrderEvent(getOrderEventStub());

        logger.info("Sending the list of order ({}) events to the client", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderEventList);
    }

    /**
     * Adds a new event for the specific order
     *
     * @param orderGuid order ID from the URN
     * @param body      {@link OrderEventDto} object
     * @return Response Entity with created {@link OrderEventDto} objects
     */
    @PostMapping(value = "/{actorGuid}/events", consumes = "application/vnd.softserve.event+json",
            produces = "application/vnd.softserve.event+json")
    public ResponseEntity<OrderEventDto> addEvent(@PathVariable String actorGuid, @PathVariable String orderGuid, @RequestBody @Valid OrderEventDto body) {
        logger.info("Sending the created event({}) to the client", actorGuid);

//        IOrderEvent iOrderEvent = orderService.createOrderEvent(orderGuid, actorGuid, body);

        logger.info("Event have been added: {}", body);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(body);
    }
}
