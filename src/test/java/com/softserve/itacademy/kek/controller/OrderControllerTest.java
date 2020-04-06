//package com.softserve.itacademy.kek.controller;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import org.mockito.InjectMocks;
//import org.mockito.MockitoAnnotations;
//import org.mockito.Spy;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import com.softserve.itacademy.kek.controller.utils.KekMediaType;
//import com.softserve.itacademy.kek.dto.OrderDto;
//import com.softserve.itacademy.kek.models.IOrder;
//import com.softserve.itacademy.kek.models.IOrderEvent;
//import com.softserve.itacademy.kek.models.impl.Order;
//import com.softserve.itacademy.kek.models.impl.OrderDetails;
//import com.softserve.itacademy.kek.models.impl.OrderEvent;
//import com.softserve.itacademy.kek.models.impl.OrderEventType;
//import com.softserve.itacademy.kek.models.impl.Tenant;
//import com.softserve.itacademy.kek.models.impl.TenantDetails;
//import com.softserve.itacademy.kek.models.impl.User;
//import com.softserve.itacademy.kek.models.impl.UserDetails;
//import com.softserve.itacademy.kek.services.IOrderEventService;
//import com.softserve.itacademy.kek.services.IOrderService;
//import com.softserve.itacademy.kek.services.IUserService;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Test(groups = {"unit-tests"})
//public class OrderControllerTest {
//    private final String orderJson = "{\n" +
//            "   \"tenant\":\"820671c6-7e2c-4de3-aeb8-42e6f84e6371\",\n" +
//            "   \"summary\":\"some summary\",\n" +
//            "   \"details\":{\n" +
//            "      \"payload\":\"some payload\",\n" +
//            "      \"imageUrl\":\"https://mypicture\"\n" +
//            "   }\n" +
//            "}";
//    private final String orderListJson = "{\n" +
//            "  \"list\": [\n" +
//            "    {\n" +
//            "   \"tenant\":\"820671c6-7e2c-4de3-aeb8-42e6f84e6371\",\n" +
//            "   \"summary\":\"some summary\",\n" +
//            "   \"details\":{\n" +
//            "      \"payload\":\"some payload\",\n" +
//            "      \"imageUrl\":\"https://mypicture\"\n" +
//            "   }\n" +
//            " }\n" +
//            "  ]\n" +
//            "}";
//
//    private final String eventJson = "{\n" +
//            "    \"orderId\": \"820671c6-7e2c-4de3-aeb8-42e6f84e6371\",\n" +
//            "    \"payload\": \"some payload\",\n" +
//            "    \"type\": \"STARTED\"\n" +
//            "}";
//
//    @InjectMocks
//    private OrderController controller;
//    @Spy
//    private IOrderService orderService;
//    @Spy
//    private Authentication authentication;
//    @Spy
//    private SecurityContext securityContext;
//    @Spy
//    private IOrderEventService orderEventService;
//    @Spy
//    private IUserService userService;
//    private MockMvc mockMvc;
//
//    private Order order;
//    private List<IOrder> orderList;
//    private OrderEvent orderEvent;
//    private List<IOrderEvent> orderEventList;
//    private User user;
//
//    @BeforeClass
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//        SecurityContextHolder.setContext(securityContext);
//
//        OrderDetails orderDetails = new OrderDetails();
//        orderDetails.setPayload("some payload");
//        orderDetails.setImageUrl("https://mypicture");
//
//        TenantDetails tenantDetails = new TenantDetails();
//        tenantDetails.setPayload("some payload");
//        tenantDetails.setImageUrl("https://mypicture");
//
//        UserDetails tenantOwnerDetails = new UserDetails();
//        tenantOwnerDetails.setPayload("some payload");
//        tenantOwnerDetails.setImageUrl("https://mypicture");
//
//        user = new User();
//        user.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
//        user.setEmail("name@email.com");
//
//        User tenantOwner = new User();
//        tenantOwner.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
//        tenantOwner.setName("Name");
//        tenantOwner.setNickname("nick123");
//        tenantOwner.setEmail("name@email.com");
//        tenantOwner.setPhoneNumber("380631234567");
//        tenantOwner.setUserDetails(tenantOwnerDetails);
//
//        Tenant tenant = new Tenant();
//        tenant.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
//        tenant.setTenantOwner(tenantOwner);
//        tenant.setName("TenantName");
//        tenant.setTenantDetails(tenantDetails);
//
//        order = new Order();
//        order.setTenant(tenant);
//        order.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
//        order.setSummary("some summary");
//        order.setOrderDetails(orderDetails);
//
//        orderList = new ArrayList<>();
//        orderList.add(order);
//
//        OrderEventType orderEventType = new OrderEventType();
//        orderEventType.setName("CREATED");
//
//        orderEvent = new OrderEvent();
//        orderEvent.setGuid(UUID.fromString("820671c6-7e2c-4de3-aeb8-42e6f84e6371"));
//        orderEvent.setOrder(order);
//        orderEvent.setOrderEventType(orderEventType);
//        orderEvent.setPayload("some payload");
//
//        orderEventList = new ArrayList<>();
//        orderEventList.add(orderEvent);
//    }
//
//    @Test
//    public void getOrderListTest() throws Exception {
//        when(orderService.getAll()).thenReturn(orderList);
//
//        mockMvc.perform(get("/orders"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(KekMediaType.ORDER_LIST))
//                .andExpect(jsonPath("$.list[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.list[0].tenant").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.list[0].summary").value("some summary"))
//                .andExpect(jsonPath("$.list[0].details.payload").value("some payload"))
//                .andExpect(jsonPath("$.list[0].details.imageUrl").value("https://mypicture"));
//    }
//
//    @Test
//    public void addOrderTest() throws Exception {
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.getPrincipal()).thenReturn(user.getEmail());
//        when(orderService.create(any(OrderDto.class), any(UUID.class))).thenReturn(order);
//        when(userService.getByEmail(any(String.class))).thenReturn(user);
//
//        mockMvc.perform(post("/orders")
//                .contentType(KekMediaType.ORDER_LIST)
//                .accept(KekMediaType.ORDER_LIST)
//                .content(orderListJson))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(KekMediaType.ORDER_LIST))
//                .andExpect(jsonPath("$.list[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.list[0].tenant").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.list[0].summary").value("some summary"))
//                .andExpect(jsonPath("$.list[0].details.payload").value("some payload"))
//                .andExpect(jsonPath("$.list[0].details.imageUrl").value("https://mypicture"));
//    }
//
//    @Test
//    public void getOrderTest() throws Exception {
//        when(orderService.getByGuid(any(UUID.class))).thenReturn(order);
//
//        mockMvc.perform(get("/orders/820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(KekMediaType.ORDER))
//                .andExpect(jsonPath("$.guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.tenant").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.summary").value("some summary"))
//                .andExpect(jsonPath("$.details.payload").value("some payload"))
//                .andExpect(jsonPath("$.details.imageUrl").value("https://mypicture"));
//    }
//
//    @Test
//    public void modifyOrderTest() throws Exception {
//        when(orderService.update(any(OrderDto.class), any(UUID.class))).thenReturn(order);
//
//        mockMvc.perform(put("/orders/820671c6-7e2c-4de3-aeb8-42e6f84e6371")
//                .contentType(KekMediaType.ORDER)
//                .accept(KekMediaType.ORDER)
//                .content(orderJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.tenant").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.summary").value("some summary"))
//                .andExpect(jsonPath("$.details.payload").value("some payload"))
//                .andExpect(jsonPath("$.details.imageUrl").value("https://mypicture"));
//    }
//
//    @Test
//    public void deleteOrderTest() throws Exception {
//        mockMvc.perform(delete("/orders/820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    public void getEventsTest() throws Exception {
//        when(orderEventService.getAllEventsForOrder(any(UUID.class))).thenReturn(orderEventList);
//
//        mockMvc.perform(get("/orders/820671c6-7e2c-4de3-aeb8-42e6f84e6371/events"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(KekMediaType.EVENT_LIST))
//                .andExpect(jsonPath("$.list[0].guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.list[0].orderId").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.list[0].payload").value("some payload"))
//                .andExpect(jsonPath("$.list[0].type").value("CREATED"));
//    }
//
//    @Test
//    public void addEventTest() throws Exception {
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.getPrincipal()).thenReturn(user.getEmail());
//        when(orderEventService.createOrderEvent(any(UUID.class), any(UUID.class), any(IOrderEvent.class)))
//                .thenReturn(orderEvent);
//        when(userService.getByEmail(any(String.class))).thenReturn(user);
//
//
//        mockMvc.perform(post("/orders/820671c6-7e2c-4de3-aeb8-42e6f84e6371/events")
//                .contentType(KekMediaType.EVENT)
//                .accept(KekMediaType.EVENT)
//                .content(eventJson))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(KekMediaType.EVENT))
//                .andExpect(jsonPath("$.guid").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.orderId").value("820671c6-7e2c-4de3-aeb8-42e6f84e6371"))
//                .andExpect(jsonPath("$.payload").value("some payload"))
//                .andExpect(jsonPath("$.type").value("CREATED"));
//    }
//}
