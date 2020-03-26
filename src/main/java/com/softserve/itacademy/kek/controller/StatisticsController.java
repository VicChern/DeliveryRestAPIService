package com.softserve.itacademy.kek.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.controller.utils.KekMappingValues;
import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.dto.ActorDto;
import com.softserve.itacademy.kek.dto.ListWrapperDto;
import com.softserve.itacademy.kek.dto.OrderDetailsDto;
import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.models.IActor;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.services.IActorService;
import com.softserve.itacademy.kek.services.IOrderService;

@RestController
@RequestMapping(path = "/statistics")
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class StatisticsController extends DefaultController {
    private final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    private final IOrderService orderService;
    private final IActorService actorService;

    @Autowired
    public StatisticsController(IOrderService orderService, IActorService actorService) {
        this.orderService = orderService;
        this.actorService = actorService;
    }

    private OrderDto transformOrder(IOrder order) {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto(order.getOrderDetails().getPayload(), order.getOrderDetails().getImageUrl());
        OrderDto orderDto = new OrderDto(order.getGuid(), order.getTenant().getGuid(), order.getSummary(), orderDetailsDto);

        return orderDto;
    }

    private ActorDto transformActor(IActor actor) {
        ActorDto actorDto = new ActorDto(actor.getGuid(), actor.getTenant().getGuid(), actor.getUser().getGuid(), actor.getAlias());
        return actorDto;
    }

    @GetMapping(value = KekMappingValues.GUID, produces = KekMediaType.ORDER_LIST)
//    @PreAuthorize("hasRole('TENANT') or hasRole('USER')")
    public ResponseEntity<ListWrapperDto<OrderDto>> getListOfOrdersForCurrentTenant(@PathVariable String guid) {
        logger.debug("Client requested the list of all orders");

        List<IOrder> orderList = orderService.getAllByTenantGuid(UUID.fromString(guid));
        ListWrapperDto<OrderDto> orderListDto = new ListWrapperDto<>(orderList
                .stream()
                .map(this::transformOrder)
                .collect(Collectors.toList()));

        logger.info("Sending list of all orders to the client:\n{}", orderListDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderListDto);

    }

    @GetMapping(value = KekMappingValues.ACTORS, produces = KekMediaType.ACTOR_LIST)
    public ResponseEntity<ListWrapperDto<ActorDto>> getListOfActorsForCurrentTenant(@PathVariable String guid) {
        logger.debug("Client requested the actorsList {}", guid);

        List<IActor> actorList = actorService.getAllByTenantGuid(UUID.fromString(guid));
        ListWrapperDto<ActorDto> actorListDto = new ListWrapperDto<>(actorList
                .stream()
                .map(this::transformActor)
                .collect(Collectors.toList()));
        logger.debug("Sending the actorsList {} to the client", actorListDto);
        return ResponseEntity.status(HttpStatus.OK).body(actorListDto);
    }

}
