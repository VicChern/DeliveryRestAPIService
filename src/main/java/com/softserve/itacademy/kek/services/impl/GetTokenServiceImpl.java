package com.softserve.itacademy.kek.services.impl;

import java.util.Calendar;
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

import com.softserve.itacademy.kek.services.IGetTokenService;

@Service
public class GetTokenServiceImpl implements IGetTokenService {

    private static final Logger logger = LoggerFactory.getLogger(GetTokenServiceImpl.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public String getToken(String email) {

        final UserDetails user = userDetailsService.loadUserByUsername(email);
        logger.info("Loading user for token creation: {}", user);

        final Map<String, Object> tokenData = new HashMap<>();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        tokenData.put("token_create_date", new Date().getTime());
        tokenData.put("authorities", authorities);
        tokenData.put("email", email);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        tokenData.put("token_expiration_date", calendar.getTime());

        final JwtBuilder jwtBuilder = Jwts.builder();

        jwtBuilder.setExpiration(calendar.getTime());
        jwtBuilder.setClaims(tokenData);

        logger.info("Building token for user: {}", user);

        return jwtBuilder.signWith(SignatureAlgorithm.HS512, System.getenv("KekSecurityKey")).compact();
    }

}

