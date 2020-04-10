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
import org.springframework.stereotype.Service;

import com.softserve.itacademy.kek.services.ITokenService;
import com.softserve.itacademy.kek.services.IUserService;

@Service
public class TokenServiceImpl implements ITokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Autowired
    private IUserService userService;

    @Override
    public String getToken(String email) {
        logger.info("Get token for user: {}", email);

        final Collection<? extends GrantedAuthority> authorities = userService.getAuthorities(email);

        final Map<String, Object> tokenData = new HashMap<>();

        final Instant createdDate = Instant.now();
        final Instant expirationDate = createdDate.plus(30, ChronoUnit.DAYS);

        tokenData.put("token_create_date", createdDate);
        tokenData.put("authorities", authorities);
        tokenData.put("email", email);

        tokenData.put("token_expiration_date", expirationDate);

        final JwtBuilder jwtBuilder = Jwts.builder();

        jwtBuilder.setExpiration(Date.from(expirationDate));
        jwtBuilder.setClaims(tokenData);

        logger.debug("Building token for user: {}", email);

        return jwtBuilder.signWith(SignatureAlgorithm.HS512, System.getenv("KekSecurityKey")).compact();
    }

}

