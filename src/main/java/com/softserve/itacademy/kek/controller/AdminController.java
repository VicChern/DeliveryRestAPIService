package com.softserve.itacademy.kek.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.controller.utils.KekMappingValues;
import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.controller.utils.KekPaths;
import com.softserve.itacademy.kek.controller.utils.KekRoles;
import com.softserve.itacademy.kek.dto.ListWrapperDto;
import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.dto.TenantDto;
import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.mappers.ITenantMapper;
import com.softserve.itacademy.kek.mappers.IUserMapper;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.services.IOrderService;
import com.softserve.itacademy.kek.services.ITenantService;
import com.softserve.itacademy.kek.services.IUserService;

@RestController
@RequestMapping(path = KekPaths.ADMIN)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminController extends DefaultController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final ITenantService tenantService;
    private final IUserService userService;
    private final IOrderService orderService;

    @Autowired
    public AdminController(ITenantService tenantService, IUserService userService, IOrderService orderService) {
        this.tenantService = tenantService;
        this.userService = userService;
        this.orderService = orderService;
    }

    /**
     * Get information about tenants
     *
     * @return Response Entity with a list of {@link TenantDto} objects as a JSON
     */
    @GetMapping(produces = KekMediaType.TENANT_LIST)
    @PreAuthorize(KekRoles.ADMIN)
    public ResponseEntity<ListWrapperDto<TenantDto>> getTenantList() {
        logger.info("Admin requested the list of all tenants");

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
    @PreAuthorize(KekRoles.ADMIN)
    public ResponseEntity<ListWrapperDto<UserDto>> getUserList() {
        logger.info("Admin requested the list of all users");

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

    /**
     * Deletes data about users from the DB
     *
     * @return Response HTTP.STATUS 202
     */
    @DeleteMapping(value = KekMappingValues.USERS)
    @PreAuthorize(KekRoles.ADMIN)
    public ResponseEntity<ListWrapperDto<UserDto>> deleteAllUsers() {
        logger.info("Admin requested the delete all users");

        userService.deleteAll();

        logger.info("All users was successfully deleted");

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }

    /**
     * Deletes data about tenants from the DB
     *
     * @return Response HTTP.STATUS 202
     */
    @DeleteMapping(value = KekMappingValues.TENANTS)
    @PreAuthorize(KekRoles.ADMIN)
    public ResponseEntity<ListWrapperDto<TenantDto>> deleteAllTenants() {
        logger.info("Admin requested the delete all orders");
        tenantService.deleteAll();

        logger.info("All tenants was successfully deleted");

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }

    /**
     * Deletes data about orders from the DB
     *
     * @return Response HTTP.STATUS 202
     */
    @DeleteMapping(value = KekMappingValues.ORDERS)
    @PreAuthorize(KekRoles.ADMIN)
    public ResponseEntity<ListWrapperDto<OrderDto>> deleteAllOrders() {
        logger.info("Admin requested the delete all orders");

        orderService.deleteAll();

        logger.info("All orders was successfully deleted");

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }


}
