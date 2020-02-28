package com.softserve.itacademy.kek.controller;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.dto.OrderDetailsDto;
import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.dto.OrderEventDto;
import com.softserve.itacademy.kek.dto.OrderEventListDto;
import com.softserve.itacademy.kek.dto.OrderEventTypesDto;
import com.softserve.itacademy.kek.dto.OrderListDto;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.IOrderEventType;
import com.softserve.itacademy.kek.services.IOrderEventService;
import com.softserve.itacademy.kek.services.IOrderService;


@RestController
@RequestMapping(path = "/orders")
public class OrderController extends DefaultController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final IOrderService orderService;
    private final IOrderEventService orderEventService;

    @Autowired
    public OrderController(IOrderService orderService, IOrderEventService orderEventService) {
        this.orderService = orderService;
        this.orderEventService = orderEventService;
    }

    private OrderDto transformOrder(IOrder order) {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto(order.getOrderDetails().getPayload(), order.getOrderDetails().getImageUrl());
        OrderDto orderDto = new OrderDto(order.getGuid(), order.getTenant().getGuid(), order.getSummary(), orderDetailsDto);

        return orderDto;
    }

    private OrderEventDto transformOrderEvent(IOrderEvent orderEvent) {
        return new OrderEventDto(orderEvent.getGuid(),
                transformOrder(orderEvent.getOrder()),
                orderEvent.getPayload(),
                transformOrderEventType(orderEvent.getOrderEventType()));
    }

    private OrderEventTypesDto transformOrderEventType(IOrderEventType orderEventType) {
        return OrderEventTypesDto.valueOf(orderEventType.getName());
    }

    /**
     * Get information about orders
     *
     * @return Response entity with list of {@link OrderListDto} objects as a JSON
     */
    @GetMapping(produces = KekMediaType.ORDER_LIST)
    public ResponseEntity<OrderListDto> getOrderList() {
        logger.debug("Client requested the list of all orders");

        //TODO: this will work when fixed OrderService
        List<IOrder> orderList = orderService.getAll();

        OrderListDto orderListDto = new OrderListDto(orderList
                .stream()
                .map(this::transformOrder)
                .collect(Collectors.toList()));

        logger.info("Sending list of all orders to the client:\n{}", orderListDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderListDto);
    }

    /**
     * Creates a new order
     *
     * @param newOrderDto  {@link OrderDto} order object as a JSON
     * @param customerGuid order guid from the URN
     * @return Response entity with {@link OrderListDto} object as a JSON
     */
    @PostMapping(value = "/{customerGuid}",
            consumes = KekMediaType.ORDER_LIST,
            produces = KekMediaType.ORDER_LIST)
    public ResponseEntity<OrderListDto> addOrder(@RequestBody @Valid OrderDto newOrderDto, @PathVariable String customerGuid) {
        logger.debug("Accepted requested to create a new order:\n{}", newOrderDto);

        IOrder createdOrder = orderService.create(newOrderDto, UUID.fromString(customerGuid));
        OrderDto createdOrderDto = transformOrder(createdOrder);

        logger.debug("Sending the created order to the client:\n{}", createdOrderDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new OrderListDto().addOrder(createdOrderDto));
    }


    /**
     * Returns information about the requested order
     *
     * @param guid order guid from the URN
     * @return Response Entity with {@link OrderDto} object as a JSON
     */
    @GetMapping(value = "/{guid}", produces = KekMediaType.ORDER)
    public ResponseEntity<OrderDto> getOrder(@PathVariable String guid) {
        logger.debug("Client requested the order {}", guid);

        IOrder order = orderService.getByGuid(UUID.fromString(guid));
        OrderDto orderDto = transformOrder(order);

        logger.debug("Sending the order {} to the client", orderDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderDto);
    }

    /**
     * Modifies information of the specified order
     *
     * @param guid  order guid from the URN
     * @param order order object as a JSON
     * @return Response entity with modified {@link OrderDto} object as a JSON
     */
    @PutMapping(value = "/{guid}",
            consumes = KekMediaType.ORDER,
            produces = KekMediaType.ORDER)
    public ResponseEntity<OrderDto> modifyOrder(@PathVariable String guid, @RequestBody @Valid OrderDto order) {
        logger.debug("Accepted modified order {} from the client", order);

        IOrder modifiedOrder = orderService.update(order, UUID.fromString(guid));
        OrderDto modifiedOrderDto = transformOrder(modifiedOrder);

        logger.debug("Sending the modified order {} to the client", modifiedOrderDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modifiedOrderDto);
    }

    /**
     * Removes the specified order
     *
     * @param guid order guid from the URN
     */
    @DeleteMapping("/{guid}")
    public ResponseEntity deleteOrder(@PathVariable String guid) {
        logger.debug("Accepted request to delete the order {}", guid);

        orderService.getByGuid(UUID.fromString(guid));

        logger.debug("Order {} successfully deleted", guid);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * Finds events of the specific order
     *
     * @param guid order guid from the URN
     * @return Response entity with list of the {@link OrderEventListDto} objects as a JSON
     */
    @GetMapping(value = "/{id}/events", produces = KekMediaType.EVENT)
    public ResponseEntity<OrderEventListDto> getEvents(@PathVariable String guid) {
        logger.info("Client requested all the events of the order {}", guid);

        //TODO: this will work when fixed OrderEventService

        List<IOrderEvent> events = orderEventService.getAllEventsForOrder(UUID.fromString(guid));

        OrderEventListDto orderEventListDto = new OrderEventListDto(UUID.fromString(guid),
                events
                        .stream()
                        .map(this::transformOrderEvent)
                        .collect(Collectors.toList()));

        logger.info("Sending the list of events of the order {} to the client", orderEventListDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderEventListDto);
    }

    /**
     * Adds a new event for the specific order
     *
     * @param actorGuid     actor guid from the URN
     * @param orderGuid     order guid from the URN
     * @param orderEventDto {@link OrderEventDto} object
     * @return Response Entity with created {@link OrderEventDto} objects as a JSON
     */
    @PostMapping(value = "/{actorGuid}/events",
            consumes = KekMediaType.EVENT,
            produces = KekMediaType.EVENT)
    public ResponseEntity<OrderEventDto> addEvent(@PathVariable String actorGuid,
                                                  @PathVariable String orderGuid,
                                                  @RequestBody @Valid OrderEventDto orderEventDto) {
        logger.info("Accepted requested to create a new event for the order {} created by actor {}", orderGuid, actorGuid);

//        IOrderEvent createdOrderEvent = orderService.createOrderEvent(UUID.fromString(orderGuid),
//                UUID.fromString(actorGuid),
//                orderEventDto);
//        OrderEventDto createdOrderEventDto = transformOrderEvent(createdOrderEvent);
// TODO: 28.02.2020  
        logger.info("Sending the created order event to the client:\n{}", orderEventDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderEventDto);
    }
}
