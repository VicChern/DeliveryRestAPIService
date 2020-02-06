package com.softserve.itacademy.kek.controller;

import org.hamcrest.Matchers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Test(groups = {"unit-tests"})
public class TenantControllerTest {

    @InjectMocks
    private TenantController controller;

    private MockMvc mockMvc;

    @BeforeTest
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getTenantListTest() throws Exception {
        mockMvc.perform(get("/tenants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value(Matchers.contains("1", "2", "3")))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void getTenantTest() throws Exception {
        mockMvc.perform(get("/tenants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value("1"))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void addTenantTest() throws Exception {
        mockMvc.perform(post(("/tenants"))
                .content("{\"item\": \"value\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value("value"));
    }

    @Test
    public void modifyTenantTest() throws Exception {
        mockMvc.perform(put("/tenants/2")
                .content("{\"item\": \"value\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value("value"));
    }

    @Test
    public void deleteTenantTest() throws Exception {
        mockMvc.perform(delete("/tenants/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value("3"))
                .andExpect(jsonPath("$.status").value("deleted"));
    }

    @Test
    public void getTenantPropertiesTest() throws Exception {
        mockMvc.perform(get("/tenants/1/properties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value("1"))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void addTenantPropertiesTest() throws Exception {
        mockMvc.perform(post("/tenants/1/properties")
                .content("{'item': 'some specific properties'}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value("some specific properties"));
    }

    @Test
    public void getTenantPropertyTest() throws Exception {
        mockMvc.perform(get("/tenants/1/properties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value("1"))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void modifyTenantPropertyTest() throws Exception {
        mockMvc.perform(put("/tenants/2/properties/2")
                .content("{\"item\": \"value\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value("value"));
    }

    @Test
    public void deleteTenantPropertyTest() throws Exception {
        mockMvc.perform(delete("/tenants/3/properties/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value("3"))
                .andExpect(jsonPath("$.status").value("deleted"));
    }

    @Test
    public void getTenantAddressesTest() throws Exception {
        mockMvc.perform(get("/tenants/1/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value("1"))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void addTenantAddressesTest() throws Exception {
        mockMvc.perform(post("/tenants/1/addresses")
                .content("{'item': '15v, Leipzigzskaya st, Kiev'}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value("15v, Leipzigzskaya st, Kiev"));
    }

    @Test
    public void getTenantAddressTest() throws Exception {
        mockMvc.perform(get("/tenants/1/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value("1"))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void modifyTenantAddressTest() throws Exception {
        mockMvc.perform(put("/tenants/2/addresses/2")
                .content("{\"item\": \"value\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value("value"));
    }

    @Test
    public void deleteTenantAddressTest() throws Exception {
        mockMvc.perform(delete("/tenants/3/addresses/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantID").value("3"))
                .andExpect(jsonPath("$.status").value("deleted"));
    }

}

