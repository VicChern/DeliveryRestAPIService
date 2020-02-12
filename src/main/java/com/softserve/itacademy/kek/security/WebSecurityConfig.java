package com.softserve.itacademy.kek.security;

import com.auth0.AuthenticationController;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.softserve.itacademy.kek.controller.LogoutController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

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
     * This is base url path to our project
     */
    @Value(value = "${base.url.path}")
    private String baseURL;

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutController();
    }

    @Bean
    public AuthenticationController authenticationController() {
        JwkProvider jwkProvider = new JwkProviderBuilder(domain).build();
        return AuthenticationController.newBuilder(domain, clientId, clientSecret)
                .withJwkProvider(jwkProvider)
                .build();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .authorizeRequests()
                .antMatchers(baseURL + "/profile/**")
                .authenticated()
                .and()
                .formLogin()
                .loginPage(baseURL + "/login")
                .successForwardUrl(baseURL + "/profile")
                .and()
                .logout()
                .logoutUrl(baseURL + "/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .permitAll()
        ;
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
