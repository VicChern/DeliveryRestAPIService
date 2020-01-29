package com.softserve.itacademy.kek.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth0")
public class Auth0Controller {
    @GetMapping
    public String getSecuredPage() {
        return "Auth0 doesn't work!";
    }

}