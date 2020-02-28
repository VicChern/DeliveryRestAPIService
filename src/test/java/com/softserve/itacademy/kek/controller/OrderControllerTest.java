package com.softserve.itacademy.kek.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderDetails;
import com.softserve.itacademy.kek.models.impl.OrderEvent;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.TenantDetails;
import com.softserve.itacademy.kek.services.IOrderEventService;
import com.softserve.itacademy.kek.services.IOrderService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Test(groups = {"unit-tests"})
public class OrderControllerTest {
    private final String orderJson = "{\n" +
            "   \"tenantGuid\":\"820671c6-7e2c-4de3-aeb8-42e6f84e6371\",\n" +
            "   \"summary\":\"some summary\",\n" +
            "   \"details\":{\n" +
            "      \"payload\":\"some payload\",\n" +
            "      \"imageUrl\":\"https://mypicture\"\n" +
            "   }\n" +
            "}";

    private final String orderEventJson = "{\n" +
            "    \"order\":{\n" +
            "       \"tenantGuid\":\"820671c6-7e2c-4de3-aeb8-42e6f84e6371\",\n" +
            "       \"guid\":\"820671c6-7e2c-4de3-aeb8-42e6f84e6371\",\n" +
            "       \"summary\":\"some summary\",\n" +
            "       \"details\":{\n" +
            "          \"payload\":\"some payload\",\n" +
            "          \"imageUrl\":\"https://mypicture\"\n" +
            "    \"payload\":\"some payload\",\n" +
            "    \"type\":\"CREATED\",\n" +
            "}";

    @InjectMocks
    private OrderController controller;
    @Spy
    private IOrderService orderService;
    @Spy
    private IOrderEventService orderEventService;
    private MockMvc mockMvc;

    private Order order;
    private List<IOrder> orderList;
    private OrderEvent orderEvent;
    private List<IOrderEvent> orderEventList;
    private Tenant tenant;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setPayload("some payload");
        orderDetails.setImageUrl("https://mypicture");

        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setPayload("some payload");
        tenantDetails.setImageUrl("https://mypicture");

        tenant = new Tenant();
        tenant.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
        //TODO: fix it
//        tenant.setTenantOwner();
        tenant.setName("TenantName");
        tenant.setTenantDetails(tenantDetails);

        order = new Order();
        order.setTenant(tenant);
        order.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
        order.setSummary("some summary");
        order.setOrderDetails(orderDetails);

        orderList = new ArrayList<>();
        orderList.add(order);

        orderEvent = new OrderEvent();
        orderEvent.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
        orderEvent.setOrder(order);

        orderEventList = new ArrayList<>();
        orderEventList.add(orderEvent);
    }

    @Test
    public void getOrderListTest() throws Exception {
        when(orderService.getAll()).thenReturn(orderList);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.order+json"))
                .andExpect(jsonPath("$.orderList[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.orderList[0].tenantGuid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.orderList[0].summary").value("some summary"))
                .andExpect(jsonPath("$.orderList[0].details.payload").value("some payload"))
                .andExpect(jsonPath("$.orderList[0].details.imageUrl").value("https://mypicture"));
    }

    @Test
    public void addOrderTest() throws Exception {
        when(orderService.create(any(OrderDto.class), any(UUID.class))).thenReturn(order);

        mockMvc.perform(post("/orders")
                .contentType("application/vnd.softserve.order+json")
                .accept("application/vnd.softserve.order+json")
                .content(orderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderList[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.orderList[0].tenantGuid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.orderList[0].summary").value("some summary"))
                .andExpect(jsonPath("$.orderList[0].details.payload").value("some payload"))
                .andExpect(jsonPath("$.orderList[0].details.imageUrl").value("https://mypicture"));
    }

    @Test
    public void getOrderTest() throws Exception {
        when(orderService.getByGuid(any(UUID.class))).thenReturn(order);

        mockMvc.perform(get("/orders/820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.order+json"))
                .andExpect(jsonPath("$.orderList[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.orderList[0].tenantGuid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.orderList[0].summary").value("some summary"))
                .andExpect(jsonPath("$.orderList[0].details.payload").value("some payload"))
                .andExpect(jsonPath("$.orderList[0].details.imageUrl").value("https://mypicture"));
    }

    @Test
    public void modifyOrderTest() throws Exception {
        when(orderService.update(any(OrderDto.class), any(UUID.class))).thenReturn(order);

        mockMvc.perform(put("/orders/820671c6-7e2c-4de3-aeb8-42e6f84e6371")
                .contentType("application/vnd.softserve.order+json")
                .accept("application/vnd.softserve.order+json")
                .content(orderJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderList[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.orderList[0].tenantGuid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.orderList[0].summary").value("some summary"))
                .andExpect(jsonPath("$.orderList[0].details.payload").value("some payload"))
                .andExpect(jsonPath("$.orderList[0].details.imageUrl").value("https://mypicture"));
    }

    @Test
    public void deleteOrderTest() throws Exception {
        mockMvc.perform(delete("/orders/820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getEventsTest() throws Exception {
        when(orderEventService.getAllEventsForOrder(any(UUID.class))).thenReturn(orderEventList);

        mockMvc.perform(get("/orders/820671c6-7e2c-4de3-aeb8-42e6f84e6371/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.event+json"))
                .andExpect(jsonPath("$.orderEventList[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.orderEventList[0].order.tenantGuid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.orderEventList[0].order.summary").value("some summary"))
                .andExpect(jsonPath("$.orderEventList[0].order.details.payload").value("some payload"))
                .andExpect(jsonPath("$.orderEventList[0].order.details.imageUrl").value("https://mypicture"))
                .andExpect(jsonPath("$.orderEventList[0].payload").value("some payload"))
                .andExpect(jsonPath("$.orderEventList[0].type").value("CREATED"));
    }

//    @Test
//    public void addEventTest() throws Exception {
//        when(orderEventService.create(any(IOrderEvent.class), any(UUID.class)));
//
//        mockMvc.perform(post("/orders/820671c6-7e2c-4de3-aeb8-42e6f84e6371/events")
//                .contentType("application/vnd.softserve.event+json")
//                .accept("application/vnd.softserve.event+json")
//                .content(orderEventJson))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType("application/vnd.softserve.event+json"))
//                .andExpect(jsonPath("$.orderEventList[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.orderEventList[0].order.tenantGuid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.orderEventList[0].order.summary").value("some summary"))
//                .andExpect(jsonPath("$.orderEventList[0].order.details.payload").value("some payload"))
//                .andExpect(jsonPath("$.orderEventList[0].order.details.imageUrl").value("https://mypicture"))
//                .andExpect(jsonPath("$.orderEventList[0].payload").value("some payload"))
//                .andExpect(jsonPath("$.orderEventList[0].type").value("CREATED"));
//    }
}
