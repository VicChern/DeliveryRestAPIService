package com.softserve.itacademy.kek.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.configuration.WebMvcConfig;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {WebMvcConfig.class, WebSecurityConfig.class, PersistenceTestConfig.class})
@WebAppConfiguration
public class WebSecurityConfigTestIT extends AbstractTestNGSpringContextTests {

    @Value(value = "${redirect.URL.when.not.authenticated}")
    private String redirectUrlWhenNotAuthenticated;

    @Value(value = "${requested.page.URL}")
    private String requestedPageURL;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeMethod(groups = {"integration-tests"})
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test(groups = {"integration-tests"})
    public void routeRedirectWhenNotAuthenticated() throws Exception {
        mvc.perform(get(requestedPageURL))
                .andExpect(status().isNotFound());
    }


}
