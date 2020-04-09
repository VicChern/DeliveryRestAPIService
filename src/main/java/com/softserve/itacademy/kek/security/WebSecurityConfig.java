package com.softserve.itacademy.kek.security;

import com.auth0.AuthenticationController;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:server.properties")
@ComponentScan("com.softserve.itacademy.kek.security")
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
    private KekAuthenticationProvider authProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Bean
    public AuthenticationController authenticationController() {
        JwkProvider jwkProvider = new JwkProviderBuilder(domain).build();
        return com.auth0.AuthenticationController.newBuilder(domain, clientId, clientSecret)
                .withJwkProvider(jwkProvider)
                .build();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandlerImpl();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationFilter authenticationFilter() throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(
                new OrRequestMatcher(
                        new AntPathRequestMatcher("/api/v1/orders/**"),
                        new AntPathRequestMatcher("/api/v1/tenants/**"),
                        new AntPathRequestMatcher("/api/v1/users/**"),
                        new AntPathRequestMatcher("/api/v1/profile"),
                        new AntPathRequestMatcher("/api/v1/statistics/**")
                )
        );
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

/*        http.authorizeRequests().anyRequest().authenticated()
                .and().httpBasic();*/

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .and()
                .formLogin()
                .loginPage(loginURL)
                .successForwardUrl(profileURL)
                .and()
                .logout()
                .logoutUrl(logoutURL)
                .logoutSuccessHandler(logoutSuccessHandler())
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
