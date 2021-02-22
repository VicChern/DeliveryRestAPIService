package com.vicchern.deliveryservice.controller;

import com.vicchern.deliveryservice.controller.utils.DeliveryServiceMediaType;
import com.vicchern.deliveryservice.models.IOrder;
import com.vicchern.deliveryservice.models.ITenant;
import com.vicchern.deliveryservice.models.IUser;
import com.vicchern.deliveryservice.models.impl.*;
import com.vicchern.deliveryservice.services.IOrderService;
import com.vicchern.deliveryservice.services.ITenantService;
import com.vicchern.deliveryservice.services.IUserService;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminControllerTest {

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
    private AdminController controller;
    @Spy
    private SecurityContext securityContext;
    @Spy
    private ITenantService tenantService;
    @Spy
    private IUserService userService;
    @Spy
    IOrderService orderService;
    private MockMvc mockMvc;

    private User user;
    private List<IUser> userList;
    private Tenant tenant;
    private List<ITenant> tenantList;
    private Order order;
    private List<IOrder> orderList;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        SecurityContextHolder.setContext(securityContext);

        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setPayload("some payload");
        tenantDetails.setImageUrl("https://mypicture");

        UserDetails tenantOwnerDetails = new UserDetails();
        tenantOwnerDetails.setPayload("some payload");
        tenantOwnerDetails.setImageUrl("http://awesomepicture.com");

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setPayload("some payload");
        orderDetails.setImageUrl("https://mypicture");

        user = new User();
        user.setGuid(UUID.fromString("10241624-9ea7-4777-99b5-54ab6d591c44"));
        user.setName("name");
        user.setNickname("nickname");
        user.setEmail("name@email.com");
        user.setPhoneNumber("380981234567");
        user.setUserDetails(tenantOwnerDetails);

        userList = new ArrayList<>();
        userList.add(user);

        User tenantOwner = new User();
        tenantOwner.setGuid(UUID.fromString("10241624-9ea7-4777-99b5-54ab6d591c44"));
        tenantOwner.setName("Name");
        tenantOwner.setNickname("nick123");
        tenantOwner.setEmail("name@email.com");
        tenantOwner.setPhoneNumber("380631234567");
        tenantOwner.setUserDetails(tenantOwnerDetails);

        tenant = new Tenant();
        tenant.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
        tenant.setTenantOwner(tenantOwner);
        tenant.setName("TenantName");
        tenant.setTenantDetails(tenantDetails);

        tenantList = new ArrayList<>();
        tenantList.add(tenant);

        order = new Order();
        order.setTenant(tenant);
        order.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
        order.setSummary("some summary");
        order.setOrderDetails(orderDetails);

        orderList = new ArrayList<>();
        orderList.add(order);


    }

    @Test
    public void getTenantListTest() throws Exception {
        when(tenantService.getAll()).thenReturn(tenantList);

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(DeliveryServiceMediaType.TENANT_LIST))
                .andExpect(jsonPath("$.list[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
                .andExpect(jsonPath("$.list[0].owner").value("10241624-9ea7-4777-99b5-54ab6d591c44"))
                .andExpect(jsonPath("$.list[0].name").value("TenantName"))
                .andExpect(jsonPath("$.list[0].details.payload").value("some payload"))
                .andExpect(jsonPath("$.list[0].details.imageUrl").value("https://mypicture"));
    }

    @Test
    public void getUserListTest() throws Exception {
        when(userService.getAll()).thenReturn(userList);

        mockMvc.perform(get("/admin")
                .contentType(DeliveryServiceMediaType.USER_LIST)
                .accept(DeliveryServiceMediaType.USER_LIST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list[0].guid").value("10241624-9ea7-4777-99b5-54ab6d591c44"))
                .andExpect(jsonPath("$.list[0].name").value("name"))
                .andExpect(jsonPath("$.list[0].nickname").value("nickname"))
                .andExpect(jsonPath("$.list[0].email").value("name@email.com"))
                .andExpect(jsonPath("$.list[0].phone").value("380981234567"))
                .andExpect(jsonPath("$.list[0].details.payload").value("some payload"))
                .andExpect(jsonPath("$.list[0].details.imageUrl").value("http://awesomepicture.com"));
    }

    @Test
    public void deleteAllUsersTest() throws Exception {
        mockMvc.perform(delete("/admin/users"))
                .andExpect(status().isAccepted());
    }

    @Test
    public void deleteAllTenantsTest() throws Exception {
        mockMvc.perform(delete("/admin/users"))
                .andExpect(status().isAccepted());
    }

    @Test
    public void deleteAllOrdersTest() throws Exception {
        mockMvc.perform(delete("/admin/orders"))
                .andExpect(status().isAccepted());
    }

}
