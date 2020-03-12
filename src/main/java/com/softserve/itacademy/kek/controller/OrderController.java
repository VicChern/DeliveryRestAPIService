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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
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
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.services.IOrderEventService;
import com.softserve.itacademy.kek.services.IOrderService;


@RestController
@RequestMapping(path = "/orders")
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
                orderEvent.getOrder().getGuid(),
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
    @PreAuthorize("hasRole('TENANT') or hasRole('USER') or hasRole('ACTOR')")
    public ResponseEntity<OrderListDto> getOrderList() {
        logger.debug("Client requested the list of all orders");

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
     * @param newOrderListDto {@link OrderDto} order object as a JSON
     * @return Response entity with {@link OrderListDto} object as a JSON
     */
    @PostMapping(consumes = KekMediaType.ORDER_LIST, produces = KekMediaType.ORDER_LIST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderListDto> addOrder(@RequestBody @Valid OrderListDto newOrderListDto,
                                                 Authentication authentication) {
        logger.debug("Accepted requested to create a new order:\n{}", newOrderListDto);

        final OrderListDto createdOrdersListDto = new OrderListDto();
        final IUser user = (IUser) authentication.getPrincipal();

        for (OrderDto orderDto : newOrderListDto.getOrderList()) {
            IOrder createdOrder = orderService.create(orderDto, UUID.fromString(user.getGuid().toString()));
            OrderDto createdOrderDto = transformOrder(createdOrder);

            createdOrdersListDto.addOrder(createdOrderDto);
        }

        logger.debug("Sending the created order to the client:\n{}", createdOrdersListDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrdersListDto);
    }


    /**
     * Returns information about the requested order
     *
     * @param guid order guid from the URN
     * @return Response Entity with {@link OrderDto} object as a JSON
     */
    @GetMapping(value = "/{guid}", produces = KekMediaType.ORDER)
    @PreAuthorize("hasRole('TENANT') or hasRole('USER') or hasRole('ACTOR')")
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
     * @param guid     order guid from the URN
     * @param orderDto order object as a JSON
     * @return Response entity with modified {@link OrderDto} object as a JSON
     */
    @PutMapping(value = "/{guid}",
            consumes = KekMediaType.ORDER,
            produces = KekMediaType.ORDER)
    @PreAuthorize("hasRole('TENANT') or hasRole('USER')")
    public ResponseEntity<OrderDto> modifyOrder(@PathVariable String guid, @RequestBody @Valid OrderDto orderDto) {
        logger.debug("Accepted modified order {} from the client", orderDto);

        IOrder modifiedOrder = orderService.update(orderDto, UUID.fromString(guid));
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
    @PreAuthorize("hasRole('TENANT') or hasRole('USER')")
    public ResponseEntity deleteOrder(@PathVariable String guid) {
        logger.debug("Accepted request to delete the order {}", guid);

        orderService.deleteByGuid(UUID.fromString(guid));

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
    @GetMapping(value = "/{guid}/events", produces = KekMediaType.EVENT_LIST)
    @PreAuthorize("hasRole('TENANT') or hasRole('USER') or hasRole('ACTOR')")
    public ResponseEntity<OrderEventListDto> getEvents(@PathVariable String guid) {
        logger.info("Client requested all the events of the order {}", guid);

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
     * @param orderEventDto {@link OrderEventDto} object
     * @return Response Entity with created {@link OrderEventDto} objects as a JSON
     */
    @PostMapping(value = "/{guid}/events",
            consumes = KekMediaType.EVENT,
            produces = KekMediaType.EVENT)
    @PreAuthorize("hasRole('TENANT') or hasRole('ACTOR') or hasRole('USER')")
    public ResponseEntity<OrderEventDto> addEvent(@RequestBody @Valid OrderEventDto orderEventDto,
                                                  @PathVariable String guid,
                                                  Authentication authentication) {
        final IUser user = (IUser) authentication.getPrincipal();

        logger.info("Accepted request to create a new event for the order {} created by actor\n{}",
                user.getGuid(),
                orderEventDto);

        IOrderEvent createdOrderEvent = orderEventService.createOrderEvent(UUID.fromString(guid),
                user.getGuid(),
                orderEventDto);

        OrderEventDto createdOrderEventDto = transformOrderEvent(createdOrderEvent);

        logger.info("Sending the created order event to the client:\n{}", createdOrderEventDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrderEventDto);
    }
}
