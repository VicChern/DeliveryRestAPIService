package com.softserve.itacademy.kek.controller;

import org.hamcrest.Matchers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Test(groups = {"unit-tests"})
public class OrderControllerTest {

    @InjectMocks
    private OrderController controller;

    private MockMvc mockMvc;

    @BeforeTest
    public void setup() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getOrderListTest() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderID").value(Matchers.contains("1", "2", "3")))
                .andExpect(jsonPath("$.status").value("received"));

    }

    @Test
    public void addOrderTest() throws Exception {
        mockMvc.perform(post("/orders")
                .content("{\"item\": \"value\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value("value"));

    }

    @Test
    public void getOrderTest() throws Exception {
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderID").value("1"))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void modifyOrderTest() throws Exception {
        mockMvc.perform(put("/orders/2")
                .content("{\"item\": \"value\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value("value"));

    }

    @Test
    public void deleteOrderTest() throws Exception {
        mockMvc.perform(delete("/orders/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderID").value("3"))
                .andExpect(jsonPath("$.status").value("deleted"));
    }

    @Test
    public void getEventsTest() throws Exception {
        mockMvc.perform(get("/orders/1/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderID").value("1"))
                .andExpect(jsonPath("$.status").value("received"));
    }

    @Test
    public void addEventTest() throws Exception {
        mockMvc.perform(post("/orders/1/events")
                .content("{'item': 'value'}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item").value("value"));

    }
}
