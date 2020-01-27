package com.softserve.itacademy.kek.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;

// TODO: Add logger

@RestController
@RequestMapping(path = "/users", produces = "application/json; charset=UTF-8")
public class UserController {

    // Build Response (stub, temporary method)
    private String getJSON(String id, String status) {
        JSONObject json = new JSONObject();
        json.put("userID", id);
        json.put("status", status);
        return json.toString();
    }

    /**
     * Get information about users
     *
     * @return information about users (JSON)
     */
    @GetMapping
    public ResponseEntity<String> getUserList() {
        JSONObject json = new JSONObject();
        json.append("userID", "1").append("userID", "2").append("userID", "3");
        json.put("status", "received");
        return ResponseEntity.ok(json.toString());
    }

    /**
     * Returns information about the requested user
     *
     * @param id user ID from the URN
     * @return user information as a JSON
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable String id) {
        return ResponseEntity.ok(getJSON(id, "received"));
    }

    /**
     * Creates a new user
     *
     * @return operation status as a JSON
     */
    @PostMapping
    public ResponseEntity<String> addUser() {
        return ResponseEntity.ok(getJSON("new", "added"));
    }

    /**
     * Modifies information of the specified user
     *
     * @param id user ID from the URN
     * @return operation status as a JSON
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifyUser(@PathVariable String id) {
        return ResponseEntity.ok(getJSON(id, "modified"));
    }

    /**
     * Removes the specified user
     *
     * @param id user ID from the URN
     * @return operation status as a JSON
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return ResponseEntity.ok(getJSON(id, "deleted"));
    }
}
