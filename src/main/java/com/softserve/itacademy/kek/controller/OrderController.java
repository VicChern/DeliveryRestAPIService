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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.controller.utils.KekMappingValues;
import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.controller.utils.KekPaths;
import com.softserve.itacademy.kek.controller.utils.KekRoles;
import com.softserve.itacademy.kek.dto.ListWrapperDto;
import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.dto.OrderEventDto;
import com.softserve.itacademy.kek.mappers.IOrderEventMapper;
import com.softserve.itacademy.kek.mappers.IOrderMapper;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.services.IOrderEventService;
import com.softserve.itacademy.kek.services.IOrderService;
import com.softserve.itacademy.kek.services.IUserService;


@RestController
@RequestMapping(path = KekPaths.ORDERS)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OrderController extends DefaultController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final IOrderService orderService;
    private final IOrderEventService orderEventService;
    private final IUserService userService;

    @Autowired
    public OrderController(IOrderService orderService, IOrderEventService orderEventService, IUserService userService) {
        this.orderService = orderService;
        this.orderEventService = orderEventService;
        this.userService = userService;
    }


    /**
     * Get information about orders
     *
     * @return Response entity with list of {@link OrderDto} objects as a JSON
     */
    @GetMapping(produces = KekMediaType.ORDER_LIST)
    @PreAuthorize(KekRoles.ANY_ROLE)
    public ResponseEntity<ListWrapperDto<OrderDto>> getOrderList() {
        logger.info("Client requested the list of all orders");

        List<IOrder> orderList = orderService.getAll();
        ListWrapperDto<OrderDto> orderListDto = new ListWrapperDto<>(orderList
                .stream()
                .map(IOrderMapper.INSTANCE::toOrderDto)
                .collect(Collectors.toList()));

        logger.info("Sending list of all orders to the client:\n{}", orderListDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderListDto);
    }

    /**
     * Creates a new order
     *
     * @param newOrderListDto list of {@link OrderDto} objects as a JSON
     * @return Response entity with list of {@link OrderDto} objects as a JSON
     */
    @PostMapping(consumes = KekMediaType.ORDER_LIST, produces = KekMediaType.ORDER_LIST)
    @PreAuthorize(KekRoles.USER_ADMIN)
    public ResponseEntity<ListWrapperDto<OrderDto>> addOrder(@RequestBody @Valid ListWrapperDto<OrderDto> newOrderListDto) {
        logger.info("Accepted requested to create a new order:\n{}", newOrderListDto);

        final ListWrapperDto<OrderDto> createdOrdersListDto = new ListWrapperDto<>();

        final String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final IUser user = userService.getByEmail(email);

        for (OrderDto orderDto : newOrderListDto.getList()) {
            IOrder createdOrder = orderService.create(orderDto, UUID.fromString(user.getGuid().toString()));
            OrderDto createdOrderDto = IOrderMapper.INSTANCE.toOrderDto(createdOrder);

            createdOrdersListDto.addKekItem(createdOrderDto);
        }

        logger.info("Sending the created order to the client:\n{}", createdOrdersListDto);
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
    @GetMapping(value = KekMappingValues.GUID, produces = KekMediaType.ORDER)
    @PreAuthorize(KekRoles.ANY_ROLE)
    public ResponseEntity<OrderDto> getOrder(@PathVariable String guid) {
        logger.info("Client requested the order {}", guid);

        IOrder order = orderService.getByGuid(UUID.fromString(guid));
        OrderDto orderDto = IOrderMapper.INSTANCE.toOrderDto(order);

        logger.info("Sending the order {} to the client", orderDto);
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
    @PutMapping(value = KekMappingValues.GUID,
            consumes = KekMediaType.ORDER,
            produces = KekMediaType.ORDER)
    @PreAuthorize(KekRoles.TENANT_USER_ADMIN)
    public ResponseEntity<OrderDto> modifyOrder(@PathVariable String guid, @RequestBody @Valid OrderDto orderDto) {
        logger.info("Accepted modified order {} from the client", orderDto);

        IOrder modifiedOrder = orderService.update(orderDto, UUID.fromString(guid));
        OrderDto modifiedOrderDto = IOrderMapper.INSTANCE.toOrderDto(modifiedOrder);

        logger.info("Sending the modified order {} to the client", modifiedOrderDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modifiedOrderDto);
    }

    /**
     * Removes the specified order
     *
     * @param guid order guid from the URN
     */
    @DeleteMapping(KekMappingValues.GUID)
    @PreAuthorize(KekRoles.TENANT_USER_ADMIN)
    public ResponseEntity deleteOrder(@PathVariable String guid) {
        logger.info("Accepted request to delete the order {}", guid);

        orderService.deleteByGuid(UUID.fromString(guid));

        logger.info("Order {} successfully deleted", guid);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }

    /**
     * Finds events of the specific order
     *
     * @param guid order guid from the URN
     * @return Response entity with list of the {@link OrderEventDto} objects as a JSON
     */
    @GetMapping(value = KekMappingValues.GUID_EVENTS, produces = KekMediaType.EVENT_LIST)
    @PreAuthorize(KekRoles.ANY_ROLE)
    public ResponseEntity<ListWrapperDto<OrderEventDto>> getEvents(@PathVariable String guid) {
        logger.info("Client requested all the events of the order {}", guid);

        List<IOrderEvent> events = orderEventService.getAllEventsForOrder(UUID.fromString(guid));
        ListWrapperDto<OrderEventDto> orderEventListDto = new ListWrapperDto<>(events.stream()
                .map(IOrderEventMapper.INSTANCE::toOrderEventDto)
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
    @PostMapping(value = KekMappingValues.GUID_EVENTS,
            consumes = KekMediaType.EVENT,
            produces = KekMediaType.EVENT)
    @PreAuthorize(KekRoles.ANY_ROLE)
    public ResponseEntity<OrderEventDto> addEvent(@RequestBody @Valid OrderEventDto orderEventDto,
                                                  @PathVariable String guid) {
        logger.info("Accepted request to create a new event for the order {} created by actor",
                orderEventDto);
        final String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final IUser user = userService.getByEmail(email);

        IOrderEvent createdOrderEvent = orderEventService.create(UUID.fromString(guid),
                user.getGuid(),
                orderEventDto);

        OrderEventDto createdOrderEventDto = IOrderEventMapper.INSTANCE.toOrderEventDto(createdOrderEvent);

        logger.info("Sending the created order event to the client:\n{}", createdOrderEventDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrderEventDto);
    }
}
