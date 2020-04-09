package com.softserve.itacademy.kek.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderDetails;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.TenantDetails;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.models.impl.UserDetails;
import com.softserve.itacademy.kek.services.IOrderService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminStatisticsControllerTest {

    private final String orderListJson = "{\n" +
            "  \"list\": [\n" +
            "    {\n" +
            "   \"tenant\":\"820671c6-7e2c-4de3-aeb8-42e6f84e6371\",\n" +
            "   \"summary\":\"some summary\",\n" +
            "   \"details\":{\n" +
            "      \"payload\":\"some payload\",\n" +
            "      \"imageUrl\":\"https://mypicture\"\n" +
            "   }\n" +
            " }\n" +
            "  ]\n" +
            "}";

    @InjectMocks
    private AdminStatisticsController controller;
    @Spy
    private IOrderService orderService;
    @Spy
    private SecurityContext securityContext;
    private MockMvc mockMvc;

    private Order order;
    private List<IOrder> orderList;
    private User user;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        SecurityContextHolder.setContext(securityContext);

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setPayload("some payload");
        orderDetails.setImageUrl("https://mypicture");

        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setPayload("some payload");
        tenantDetails.setImageUrl("https://mypicture");

        UserDetails tenantOwnerDetails = new UserDetails();
        tenantOwnerDetails.setPayload("some payload");
        tenantOwnerDetails.setImageUrl("https://mypicture");

        user = new User();
        user.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));

        User tenantOwner = new User();
        tenantOwner.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
        tenantOwner.setName("Name");
        tenantOwner.setNickname("nick123");
        tenantOwner.setEmail("name@email.com");
        tenantOwner.setPhoneNumber("380631234567");
        tenantOwner.setUserDetails(tenantOwnerDetails);

        Tenant tenant = new Tenant();
        tenant.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
        tenant.setTenantOwner(tenantOwner);
        tenant.setName("TenantName");
        tenant.setTenantDetails(tenantDetails);

        order = new Order();
        order.setTenant(tenant);
        order.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
        order.setSummary("some summary");
        order.setOrderDetails(orderDetails);

        orderList = new ArrayList<>();
        orderList.add(order);
    }

    @Test
    public void getListOfOrdersForCurrentTenant() throws Exception {
        when(orderService.getAllByTenantGuid(any(UUID.class))).thenReturn(orderList);

        mockMvc.perform(get("/statistics/820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(KekMediaType.ORDER_LIST))
                .andExpect(jsonPath("$.list[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.list[0].tenant").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.list[0].summary").value("some summary"))
                .andExpect(jsonPath("$.list[0].details.payload").value("some payload"))
                .andExpect(jsonPath("$.list[0].details.imageUrl").value("https://mypicture"));

    }

}
