package com.softserve.itacademy.kek.controller;

import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;

import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.TenantDetails;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.models.impl.UserDetails;

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
    private AdminController controller;
    @Spy
    private SecurityContext securityContext;
    private MockMvc mockMvc;

    private User user;

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

    }

}
