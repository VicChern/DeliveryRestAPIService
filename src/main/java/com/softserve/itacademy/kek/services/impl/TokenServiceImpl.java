package com.softserve.itacademy.kek.services.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.services.ITokenService;

@Service
public class TokenServiceImpl implements ITokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public String getToken(String email) {

        final UserDetails user = userDetailsService.loadUserByUsername(email);
        logger.info("Loading user for token creation: {}", user);

        final Map<String, Object> tokenData = new HashMap<>();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        final Instant createdDate = Instant.now();
        final Instant expirationDate = createdDate.plus(1, ChronoUnit.MONTHS);

        tokenData.put("token_create_date", createdDate);
        tokenData.put("authorities", authorities);
        tokenData.put("email", email);

        tokenData.put("token_expiration_date", expirationDate);

        final JwtBuilder jwtBuilder = Jwts.builder();

        jwtBuilder.setExpiration(Date.from(expirationDate));
        jwtBuilder.setClaims(tokenData);

        logger.info("Building token for user: {}", user);

        return jwtBuilder.signWith(SignatureAlgorithm.HS512, System.getenv("KekSecurityKey")).compact();
    }

}

