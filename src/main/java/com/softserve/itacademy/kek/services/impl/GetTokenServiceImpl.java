package com.softserve.itacademy.kek.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.services.IGetTokenService;

@Service
public class GetTokenServiceImpl implements IGetTokenService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public String getToken(String email) {

        final UserDetails user = userDetailsService.loadUserByUsername(email);

        final Map<String, Object> tokenData = new HashMap<>();

        tokenData.put("authorities", user.getAuthorities());
        tokenData.put("email", email);
//        tokenData.put("details", user.toString());

        tokenData.put("token_create_date", new Date().getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 100);
        tokenData.put("token_expiration_date", calendar.getTime());

        final JwtBuilder jwtBuilder = Jwts.builder();

        jwtBuilder.setExpiration(calendar.getTime());
        jwtBuilder.setClaims(tokenData);

        String key = "abc123";

        return jwtBuilder.signWith(SignatureAlgorithm.HS512, key).compact();
    }

}

