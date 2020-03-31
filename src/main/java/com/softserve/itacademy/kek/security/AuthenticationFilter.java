package com.softserve.itacademy.kek.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.softserve.itacademy.kek.models.impl.UserDetails;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    AuthenticationFilter(final RequestMatcher requiresAuth) {
        super(requiresAuth);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException {

        String token = httpServletRequest.getHeader(AUTHORIZATION);

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("abc123"))
                .parseClaimsJws(token).getBody();

        String roles = claims.get("authorities").toString()
                .replaceAll("[\\[\\]]", "")
                .replaceAll("\\{authority=", "")
                .replaceAll("\\}", "");

        List<GrantedAuthority> list = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);

        //TODO: investigate userdetails in requestAuthentication? and data parsing from token

        Authentication requestAuthentication = new UsernamePasswordAuthenticationToken(claims.get("email"), null ,  list);

        return getAuthenticationManager().authenticate(requestAuthentication);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}