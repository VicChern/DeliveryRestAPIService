package com.softserve.itacademy.kek.security;

import com.auth0.AuthenticationController;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.softserve.itacademy.kek.controller.AuthController;
import com.softserve.itacademy.kek.services.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:server.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * This is your auth0 domain (tenant you have created when registering with auth0 - account name)
     */
    private final String domain = System.getenv("KekDomain");

    /**
     * This is the client id of auth0 application
     */
    private final String clientId = System.getenv("KekId");

    /**
     * This is the client secret of auth0 application
     */
    private final String clientSecret = System.getenv("KekWord");

    /**
     * This is url path to user info page
     */

    @Value(value = "${secured.profile.url}")
    private String profileURL;


    @Value(value = "${login.url}")
    private String loginURL;

    @Value(value = "${logout.url}")
    private String logoutURL;

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    UserDetailsService getuserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public AuthenticationController authenticationController() {
        JwkProvider jwkProvider = new JwkProviderBuilder(domain).build();
        return com.auth0.AuthenticationController.newBuilder(domain, clientId, clientSecret)
                .withJwkProvider(jwkProvider)
                .build();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .authorizeRequests()
//                .antMatchers(baseURL + "/profile/**")
//                .authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/profile").hasAnyRole("USER")
                .and()
                .formLogin()
                .loginPage(loginURL)
                .successForwardUrl(profileURL)
                .and()
                .logout()
                .logoutUrl(logoutURL)
                .permitAll();
    }

    public String getDomain() {
        return domain;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
