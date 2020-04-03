package com.softserve.itacademy.kek.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    AuthenticationFilter(final RequestMatcher requiresAuth) {
        super(requiresAuth);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException {

        String token = httpServletRequest.getHeader(AUTHORIZATION);
        List<GrantedAuthority> roles = new ArrayList<>();

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(System.getenv("KekSecurityKey")))
                .parseClaimsJws(token).getBody();

        String authorities = claims.get("authorities").toString();

        if ( authorities.contains("ROLE_USER") ) {
            roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else if (authorities.contains("ROLE_TENANT")) {
            roles.add(new SimpleGrantedAuthority("ROLE_TENANT"));
        } else if (authorities.contains("ROLE_ACTOR")) {
            roles.add(new SimpleGrantedAuthority("ROLE_ACTOR"));
        }

        Authentication requestAuthentication = new UsernamePasswordAuthenticationToken(claims.get("email"), null ,  roles);

        return getAuthenticationManager().authenticate(requestAuthentication);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}