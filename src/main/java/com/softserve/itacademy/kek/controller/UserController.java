package com.softserve.itacademy.kek.controller;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.dto.DetailsDto;
import com.softserve.itacademy.kek.dto.UserDto;

@RestController
@RequestMapping(path = "/users")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserController extends DefaultController {
    final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * Temporary method for UserDto stub
     *
     * @return {@link UserDto} stub
     */
    private UserDto getUserDtoStub() {
        DetailsDto detailsDto = new DetailsDto("some payload", "http://awesomepicture.com");
        UserDto userDto = new UserDto("guid12345qwert", "Petro", "pict", "pict@email.com", "(098)123-45-67", detailsDto);
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
     * @return Response entity with list of {@link UserDto} objects as a JSON
     */
    @GetMapping(produces = "application/vnd.softserve.user+json")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<List<UserDto>> getUserList() {
        logger.info("Client requested the list of all users");

        List<UserDto> userList = new ArrayList<>();
        userList.add(getUserDtoStub());

        logger.info("Sending list of all users to the client:\n" + userList);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    /**
     * Creates a new user
     *
     * @param body {@link UserDto} object as a JSON
     * @return Response entity with {@link UserDto} object as a JSON
     */
    @PostMapping(consumes = "application/vnd.softserve.user+json", produces = "application/vnd.softserve.user+json")
    @PreAuthorize("hasRole('TENANT') or hasRole('USER')")
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid UserDto body) {
        logger.info("Accepted requested to create a new user:\n" + body);
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }

    /**
     * Returns information about the requested user
     *
     * @param id user ID from the URN
     * @return Response entity with {@link UserDto} object as a JSON
     */
    @GetMapping(value = "/{id}", produces = "application/vnd.softserve.user+json")
    @PreAuthorize("hasRole('TENANT') or hasRole('USER')")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        logger.info("Client requested the user " + id);

        UserDto userDto = getUserDtoStub();

        logger.info("Sending the user to the client:\n" + userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> modifyUser(@PathVariable String id, @RequestBody @Valid UserDto body) {
        logger.info("Accepted modified user from the client:\n" + body);
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }

    /**
     * Removes the specified user
     *
     * @param id user ID from the URN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TENANT') or hasRole('USER')")
    public ResponseEntity deleteUser(@PathVariable String id) {
        logger.info("Accepted request to delete the user " + id);
        logger.info("User (" + id + ") successfully deleted");

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Finds addresses of the specific user
     *
     * @param id user ID from the URN
     * @return Response Entity with list of the {@link AddressDto} objects as a JSON
     */
    @GetMapping(value = "/{id}/addresses", produces = "application/vnd.softserve.address+json")
    @PreAuthorize("hasRole('TENANT') or hasRole('USER')")
    public ResponseEntity<List<AddressDto>> getUserAddresses(@PathVariable String id) {
        logger.info("Client requested all the addresses of the employee " + id);

        List<AddressDto> addressesList = new ArrayList<>();
        addressesList.add(getAddressDtoStub());

        logger.info("Sending the list of addresses of the user " + id + " to the client:\n" + addressesList);
        return new ResponseEntity<>(addressesList, HttpStatus.OK);
    }

    /**
     * Adds a new addresses for the specific user
     *
     * @param id   user ID from the URN
     * @param body list of address objects as a JSON
     * @return Response entity with list of the {@link AddressDto} objects as a JSON
     */
    @PostMapping(value = "/{id}/addresses", consumes = "application/vnd.softserve.address+json",
            produces = "application/vnd.softserve.address+json")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AddressDto>> addUserAddresses(@PathVariable String id,
                                                             @RequestBody @Valid List<AddressDto> body) {
        logger.info("Accepted requested to create a new addresses for user:" + id + ":\n" + body);
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }

    /**
     * Finds addresses of the specific user
     *
     * @param id       user ID from the URN
     * @param addrGuid address ID from the URN
     * @return Response entity with {@link AddressDto} object as a JSON
     */
    @GetMapping(value = "/{id}/addresses/{addrguid}", produces = "application/vnd.softserve.address+json")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressDto> getUserAddress(@PathVariable("id") String id, @PathVariable("addrguid") String addrGuid) {
        logger.info("Client requested the address " + addrGuid + " of the employee " + id);

        AddressDto addressDto = getAddressDtoStub();

        logger.info("Sending the address of the user " + id + " to the client:\n" + addressDto);
        return new ResponseEntity<>(addressDto, HttpStatus.OK);
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressDto> modifyUserAddress(@PathVariable("id") String id,
                                                        @PathVariable("addrguid") String addrGuid,
                                                        @RequestBody @Valid AddressDto body) {
        logger.info("Accepted modified address of the user " + id + " from the client:\n" + body);
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }

    /**
     * Deletes the specific user address
     *
     * @param id       user ID from the URN
     * @param addrGuid address ID from the URN
     */
    @DeleteMapping("/{id}/addresses/{addrguid}")
    @PreAuthorize("hasRole('TENANT') or hasRole('USER')")
    public ResponseEntity deleteUserAddress(@PathVariable("id") String id, @PathVariable("addrguid") String addrGuid) {
        logger.info("Accepted request to delete the address " + addrGuid + " ot the user " + id);

        logger.info("the address " + addrGuid + " ot the user " + id + " successfully deleted");

        return new ResponseEntity(HttpStatus.OK);
    }
}
