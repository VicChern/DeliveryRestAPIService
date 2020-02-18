package com.softserve.itacademy.kek.controller;

import javax.validation.Valid;
import java.util.UUID;

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

import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.dto.AddressListDto;
import com.softserve.itacademy.kek.dto.DetailsDto;
import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.dto.UserListDto;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.services.IUserService;

@RestController
@RequestMapping(path = "/users")
public class UserController extends DefaultController {
    final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    private UserDto transform(IUser user) {
        DetailsDto userDetailsDto = new DetailsDto(user.getUserDetails().getPayload(), user.getUserDetails().getImageUrl());
        UserDto userDto = new UserDto(user.getGuid(), user.getName(), user.getNickname(), user.getEmail(),
                user.getPhoneNumber(), userDetailsDto);
        return userDto;
    }

    /**
     * Temporary method for UserDto stub
     *
     * @return {@link UserDto} stub
     */
    private UserDto getUserDtoStub() {
        DetailsDto detailsDto = new DetailsDto("some payload", "http://awesomepicture.com");
        UserDto userDto = new UserDto(UUID.fromString("guid12345qwert"), "Petro", "pict", "pict@email.com",
                "(098)123-45-67", detailsDto);
        return userDto;
    }

    /**
     * Temporary method for AddressDto
     *
     * @return {@link AddressDto} stub
     */
    private AddressDto getAddressDtoStub() {
        AddressDto addressDto = new AddressDto("guid12345qwert", "alias", "Leipzigzskaya 15v", "Some notes...");
        return addressDto;
    }

    /**
     * Get information about users
     *
     * @return Response entity with list of {@link UserListDto} objects as a JSON
     */
    @GetMapping(produces = "application/vnd.softserve.userList+json")
    public ResponseEntity<UserListDto> getUserList() {
        logger.info("Client requested the list of all users");

        UserListDto userList = new UserListDto();
        userList.addUser(getUserDtoStub());

        logger.info("Sending list of all users to the client:\n{}", userList);
        return ResponseEntity
                .ok()
                .body(userList);
    }

    /**
     * Creates a new user
     *
     * @param body {@link UserListDto} object as a JSON
     * @return Response entity with {@link UserListDto} object as a JSON
     */
    @PostMapping(consumes = "application/vnd.softserve.userList+json",
            produces = "application/vnd.softserve.userList+json")
    public ResponseEntity<UserListDto> addUser(@RequestBody @Valid UserListDto body) {
        logger.info("Accepted requested to create a new user:\n{}", body);

        IUser createdUser = userService.create(body.getUserList().get(0));
        UserDto userDto = transform(createdUser);

        body = new UserListDto();
        body.addUser(userDto);

        logger.info("Sending the created users to the client:\n{}", body);
        return ResponseEntity
                .ok()
                .body(body);
    }

    /**
     * Returns information about the requested user
     *
     * @param id user ID from the URN
     * @return Response entity with {@link UserDto} object as a JSON
     */
    @GetMapping(value = "/{id}", produces = "application/vnd.softserve.user+json")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        logger.info("Client requested the user {}", id);

        UserDto userDto = getUserDtoStub();

        logger.info("Sending the user to the client:\n{}", userDto);
        return ResponseEntity
                .ok()
                .body(userDto);
    }

    /**
     * Modifies information of the specified user
     *
     * @param id   user ID from the URN
     * @param body user object as a JSON
     * @return Response entity with {@link UserDto} object as a JSON
     */
    @PutMapping(value = "/{id}", consumes = "application/vnd.softserve.user+json",
            produces = "application/vnd.softserve.user+json")
    public ResponseEntity<UserDto> modifyUser(@PathVariable String id, @RequestBody @Valid UserDto body) {
        logger.info("Accepted modified user from the client:\n{}", body);

        logger.info("Sending the modified user to the client:\n{}", body);
        return ResponseEntity
                .ok()
                .body(body);
    }

    /**
     * Removes the specified user
     *
     * @param id user ID from the URN
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable String id) {
        logger.info("Accepted request to delete the user {}", id);

        logger.info("User ({}) successfully deleted", id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Finds addresses of the specific user
     *
     * @param id user ID from the URN
     * @return Response Entity with list of the {@link AddressListDto} objects as a JSON
     */
    @GetMapping(value = "/{id}/addresses", produces = "application/vnd.softserve.addressList+json")
    public ResponseEntity<AddressListDto> getUserAddresses(@PathVariable String id) {
        logger.info("Client requested all the addresses of the employee {}", id);

        AddressListDto addressesList = new AddressListDto();
        addressesList.addAddress(getAddressDtoStub());

        logger.info("Sending the list of addresses of the user {} to the client:\n", addressesList);
        return ResponseEntity
                .ok()
                .body(addressesList);
    }

    /**
     * Adds a new addresses for the specific user
     *
     * @param id   user ID from the URN
     * @param body list of address objects as a JSON
     * @return Response entity with list of the {@link AddressListDto} objects as a JSON
     */
    @PostMapping(value = "/{id}/addresses", consumes = "application/vnd.softserve.addressList+json",
            produces = "application/vnd.softserve.addressList+json")
    public ResponseEntity<AddressListDto> addUserAddresses(@PathVariable String id,
                                                           @RequestBody @Valid AddressListDto body) {
        logger.info("Accepted requested to create a new addresses for user:{}:\n", body);

        logger.info("Sending the created users's addresses to the client:\n{}", body);
        return ResponseEntity
                .ok()
                .body(body);
    }

    /**
     * Finds addresses of the specific user
     *
     * @param id       user ID from the URN
     * @param addrGuid address ID from the URN
     * @return Response entity with {@link AddressDto} object as a JSON
     */
    @GetMapping(value = "/{id}/addresses/{addrguid}", produces = "application/vnd.softserve.address+json")
    public ResponseEntity<AddressDto> getUserAddress(@PathVariable("id") String id, @PathVariable("addrguid") String addrGuid) {
        logger.info("Client requested the address {} of the employee {}", addrGuid, id);

        AddressDto addressDto = getAddressDtoStub();

        logger.info("Sending the address of the user {} to the client:\n{}", id, addressDto);
        return ResponseEntity
                .ok()
                .body(addressDto);
    }

    /**
     * Modifies the specific user address tenant property
     *
     * @param id       user ID from the URN
     * @param addrGuid address ID from the URN
     * @param body     address object as a JSON
     * @return Response Entity with {@link AddressDto} object as a JSON
     */
    @PutMapping(value = "/{id}/addresses/{addrguid}", consumes = "application/vnd.softserve.address+json",
            produces = "application/vnd.softserve.address+json")
    public ResponseEntity<AddressDto> modifyUserAddress(@PathVariable("id") String id,
                                                        @PathVariable("addrguid") String addrGuid,
                                                        @RequestBody @Valid AddressDto body) {
        logger.info("Accepted modified address of the user {} from the client:\n{}", id, body);

        logger.info("Sending the modified address of the user {} to the client:\n{}", id, body);
        return ResponseEntity
                .ok()
                .body(body);
    }

    /**
     * Deletes the specific user address
     *
     * @param id       user ID from the URN
     * @param addrGuid address ID from the URN
     */
    @DeleteMapping("/{id}/addresses/{addrguid}")
    public ResponseEntity deleteUserAddress(@PathVariable("id") String id, @PathVariable("addrguid") String addrGuid) {
        logger.info("Accepted request to delete the address {} ot the user {}", addrGuid, id);

        logger.info("the address {} ot the user {} successfully deleted", addrGuid, id);

        return new ResponseEntity(HttpStatus.OK);
    }
}
