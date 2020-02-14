package com.softserve.itacademy.kek.controller;

import com.google.gson.Gson;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.dto.OrderDetailsDto;
import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.dto.OrderEventDto;
import com.softserve.itacademy.kek.dto.OrderEventTypesDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Test(groups = {"unit-tests"})
public class OrderControllerTest {
    private final Gson gson = new Gson();
    private OrderEventDto orderEventDto;
    private OrderDto orderDto;

    @InjectMocks
    private OrderController controller;

    private MockMvc mockMvc;

    @BeforeTest
    public void setup() {
        OrderDetailsDto orderDetails = new OrderDetailsDto("some info", "https://mypicture");
        orderDto = new OrderDto("MyTenant", "user123", "123",
                "summary", orderDetails);
        orderEventDto = new OrderEventDto("wqewqe1r1", "123",
                "some info", OrderEventTypesDto.DELIVERED);

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    }

    @Test
    public void getOrderListTest() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.order+json"))
                .andExpect(jsonPath("$[0].tenant").value("MyTenant"))
                .andExpect(jsonPath("$[0].guid").value("safgad123"))
                .andExpect(jsonPath("$[0].details.payload").value("some info"))
                .andExpect(jsonPath("$[0].details.imageUrl").value("https://mypicture"));
    }

    @Test
    public void addOrderTest() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType("application/vnd.softserve.order+json")
                .accept("application/vnd.softserve.order+json")
                .content(gson.toJson(orderDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.tenant").value("MyTenant"))
                .andExpect(jsonPath("$.guid").value("safgad123"))
                .andExpect(jsonPath("$.details.payload").value("some info"))
                .andExpect(jsonPath("$.details.imageUrl").value("https://mypicture"));
    }

    @Test
    public void getOrderTest() throws Exception {
        mockMvc.perform(get("/orders/safgad123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.order+json"))
                .andExpect(jsonPath("$.tenant").value("MyTenant"))
                .andExpect(jsonPath("$.guid").value("safgad123"))
                .andExpect(jsonPath("$.details.payload").value("some info"))
                .andExpect(jsonPath("$.details.imageUrl").value("https://mypicture"));
    }

    @Test
    public void modifyOrderTest() throws Exception {
        mockMvc.perform(put("/orders/safgad123")
                .contentType("application/vnd.softserve.order+json")
                .accept("application/vnd.softserve.order+json")
                .content(gson.toJson(orderDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.tenant").value("MyTenant"))
                .andExpect(jsonPath("$.guid").value("safgad123"))
                .andExpect(jsonPath("$.details.payload").value("some info"))
                .andExpect(jsonPath("$.details.imageUrl").value("https://mypicture"));
    }

    @Test
    public void deleteOrderTest() throws Exception {
        mockMvc.perform(delete("/orders/safgad123"))
                .andExpect(status().isOk());
    }

    @Test
    public void getEventsTest() throws Exception {
        mockMvc.perform(get("/orders/safgad123/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.event+json"))
                .andExpect(jsonPath("$[0].guid").value("wqewqe1r1"))
                .andExpect(jsonPath("$[0].orderId").value("123"))
                .andExpect(jsonPath("$[0].payload").value("some info"))
                .andExpect(jsonPath("$[0].type").value("DELIVERED"));
    }

    @Test
    public void addEventTest() throws Exception {
        mockMvc.perform(post("/orders/safgad123/events")
                .contentType("application/vnd.softserve.event+json")
                .accept("application/vnd.softserve.event+json")
                .content(gson.toJson(orderEventDto)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType("application/vnd.softserve.event+json"))
                .andExpect(jsonPath("$.guid").value("wqewqe1r1"))
                .andExpect(jsonPath("$.orderId").value("123"))
                .andExpect(jsonPath("$.payload").value("some info"))
                .andExpect(jsonPath("$.type").value("DELIVERED"));
    }
}
