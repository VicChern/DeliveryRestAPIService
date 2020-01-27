package com.softserve.itacademy.kek.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;

@RestController
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends DefaultController {

    // Build Response (temporary method)
    private String getResponse(String id, String status) {
        JSONObject response = new JSONObject();
        response.put("userID", id);
        response.put("status", status);
        return response.toString();
    }

    /**
     * Get information about users
     *
     * @return information about users (JSON)
     */
    @GetMapping
    public ResponseEntity<String> getUserList() {
        JSONObject response = new JSONObject();
        response.append("userID", "1").append("userID", "2").append("userID", "3");
        response.put("status", "received");
        return ResponseEntity.ok(response.toString());
    }

    /**
     * Returns information about the requested user
     *
     * @param id user ID from the URN
     * @return user information as a JSON
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable String id) {
        return ResponseEntity.ok(getResponse(id, "received"));
    }

    /**
     * Creates a new user
     *
     * @return operation status as a JSON
     */
    @PostMapping
    public ResponseEntity<String> addUser() {
        return ResponseEntity.ok(getResponse("new", "added"));
    }

    /**
     * Modifies information of the specified user
     *
     * @param id user ID from the URN
     * @return operation status as a JSON
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifyUser(@PathVariable String id) {
        return ResponseEntity.ok(getResponse(id,"modified"));
    }

    /**
     * Removes the specified user
     *
     * @param id user ID from the URN
     * @return operation status as a JSON
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return ResponseEntity.ok(getResponse(id, "deleted"));
    }
}
