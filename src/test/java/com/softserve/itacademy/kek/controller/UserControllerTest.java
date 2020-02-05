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
public class UserControllerTest {

    @InjectMocks
    private UserController controller;

    private MockMvc mockMvc;

    @BeforeTest
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getUserListTest() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.userID").value(Matchers.contains("1", "2", "3")))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void getUserTest() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.userID").value("1"))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void addUserTest() throws Exception {
        mockMvc.perform(post("/users")
                .content("{\"item\": \"value\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.item").value("value"));
    }

    @Test
    public void modifyUserTest() throws Exception {
        mockMvc.perform(put("/users/2")
                .content("{\"item\": \"value\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.item").value("value"));
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/users/3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.userID").value("3"))
                .andExpect(jsonPath("$.status").value("deleted"));
    }

    @Test
    public void getUserAddressesTest() throws Exception {
        mockMvc.perform(get("/users/1/addresses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.userID").value("1"))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void addUserAddressesTest() throws Exception {
        mockMvc.perform(post("/users/1/addresses")
                .content("{'item': '15v, Leipzigzskaya st, Kiev'}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.item").value("15v, Leipzigzskaya st, Kiev"));
    }

    @Test
    public void getUserAddressTest() throws Exception {
        mockMvc.perform(get("/users/1/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.userID").value("1"))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void modifyUserAddressTest() throws Exception {
        mockMvc.perform(put("/users/2/addresses/1")
                .content("{\"item\": \"value\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.item").value("value"));
    }

    @Test
    public void deleteUserAddressTest() throws Exception {
        mockMvc.perform(delete("/users/3/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.userID").value("3"))
                .andExpect(jsonPath("$.status").value("deleted"));
    }
}
