package com.softserve.itacademy.kek.controller;

import com.softserve.itacademy.kek.security.TokenAuthentication;
import com.softserve.itacademy.kek.security.TokenUtils;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/profile", produces = "application/json; charset=UTF-8")
public class ProfileInfoController extends DefaultController {

    @GetMapping
    protected ResponseEntity<String> profile(Authentication authentication) {

        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;

        JSONObject json = new JSONObject();
        json.put("profileJson", TokenUtils.claimsAsJson(tokenAuthentication.getClaims()));
        return ResponseEntity.ok(json.toString());
    }

}
