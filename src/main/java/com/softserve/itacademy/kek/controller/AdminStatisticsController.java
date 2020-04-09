package com.softserve.itacademy.kek.controller;

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
import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.mappers.IActorMapper;
import com.softserve.itacademy.kek.mappers.IOrderMapper;
import com.softserve.itacademy.kek.mappers.ITenantMapper;
import com.softserve.itacademy.kek.mappers.IUserMapper;
import com.softserve.itacademy.kek.models.IActor;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.services.IActorService;
import com.softserve.itacademy.kek.services.IOrderService;
import com.softserve.itacademy.kek.services.ITenantService;
import com.softserve.itacademy.kek.services.IUserService;

@RestController
@RequestMapping(path = "/statistics")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminStatisticsController extends DefaultController {
    private static final Logger logger = LoggerFactory.getLogger(AdminStatisticsController.class);

    private final IActorService actorService;
    private final ITenantService tenantService;
    private final IUserService userService;

    @Autowired
    public AdminStatisticsController(IActorService actorService, ITenantService tenantService, IUserService userService) {
        this.actorService = actorService;
        this.tenantService = tenantService;
        this.userService = userService;
    }

    /**
     * Get information about tenants
     *
     * @return Response Entity with a list of {@link TenantDto} objects as a JSON
     */
    @GetMapping(produces = KekMediaType.TENANT_LIST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ListWrapperDto<TenantDto>> getTenantList() {
        logger.info("Client requested the list of all tenants");

        List<ITenant> tenantList = tenantService.getAll();
        ListWrapperDto<TenantDto> tenantListDto = new ListWrapperDto<>(tenantList
                .stream()
                .map(ITenantMapper.INSTANCE::toTenantDto)
                .collect(Collectors.toList()));

        logger.info("Sending list of all tenants to the client:\n{}", tenantList);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tenantListDto);
    }

    /**
     * Get information about users
     *
     * @return Response entity with list of {@link UserDto} objects as a JSON
     */
    @GetMapping(produces = KekMediaType.USER_LIST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ListWrapperDto<UserDto>> getUserList() {
        logger.info("Client requested the list of all users");

        List<IUser> userList = userService.getAll();
        ListWrapperDto<UserDto> userListDto = new ListWrapperDto<>(userList
                .stream()
                .map(IUserMapper.INSTANCE::toUserDto)
                .collect(Collectors.toList()));

        logger.info("Sending list of all users to the client:\n{}", userListDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userListDto);
    }


    @GetMapping(value = KekMappingValues.ACTORS, produces = KekMediaType.ACTOR_LIST)
    @PreAuthorize("hasRole('TENANT') or hasRole('USER')")
    public ResponseEntity<ListWrapperDto<ActorDto>> getListOfActorsForCurrentTenant(@PathVariable String guid) {
        logger.debug("Client requested the actorsList {}", guid);

        List<IActor> actorList = actorService.getAllByTenantGuid(UUID.fromString(guid));
        ListWrapperDto<ActorDto> actorListDto = new ListWrapperDto<>(actorList
                .stream()
                .map(IActorMapper.INSTANCE::toActorDto)
                .collect(Collectors.toList()));
        logger.debug("Sending the actorsList {} to the client", actorListDto);
        return ResponseEntity.status(HttpStatus.OK).body(actorListDto);
    }

}
