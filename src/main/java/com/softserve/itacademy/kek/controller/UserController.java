package com.softserve.itacademy.kek.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController extends DefaultController {
    private Map<String, String> response = new HashMap<>();

    /**
     * Returns information about the requested user
     * @param id user ID from the URN
     * @return user information as a JSON
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Map<String, String> getUser(@PathVariable String id) {
        response.clear();

        try {
            Integer.parseInt(id);

            response.put("User ID", id);
            response.put("User status", "received");
        } catch (NumberFormatException e) {
            response.put("Error", "Could not receive the user");
            response.put("Error details", "Incorrect user ID format");
        }

        return response;
    }

    /**
     * Creates a new user
     * @return operation status as a JSON
     */
    @RequestMapping(method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Map<String, String> addUser() {
        response.clear();

        response.put("User ID", "1");
        response.put("User status", "added");
        return response;
    }

    /**
     * Modifies information of the specified user
     * @param id user ID from the URN
     * @return operation status as a JSON
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Map<String, String> modifyUser(@PathVariable String id) {
        response.clear();

        try {
            Integer.parseInt(id);

            response.put("User ID", id);
            response.put("User status", "modified");
        } catch (NumberFormatException e) {
            response.put("Error", "Could not modify the user");
            response.put("Error details", "Incorrect user ID format");
        }

        return response;
    }

    /**
     * Removes the specified user
     * @param id user ID from the URN
     * @return operation status as a JSON
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Map<String, String> removeUser(@PathVariable String id) {
        response.clear();

        try {
            Integer.parseInt(id);

            response.put("User ID", id);
            response.put("User status", "removed");
        } catch (NumberFormatException e) {
            response.put("Error", "Could not remove the user");
            response.put("Error details", "Incorrect user ID format");
        }

        return response;
    }
}
