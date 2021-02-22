package com.softserve.itacademy.kek.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.configuration.WebMvcConfig;
import com.softserve.itacademy.kek.models.enums.EventTypeEnum;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.security.WebSecurityConfig;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrder;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrderEvent;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getTenant;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {WebMvcConfig.class, WebSecurityConfig.class, PersistenceTestConfig.class})
@WebAppConfiguration
public class SseControllerTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private OrderEventTypeRepository orderEventTypeRepository;

    @Autowired
    private OrderEventRepository orderEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private OrderRepository orderRepository;

    Order order;
    private OrderEventType orderEventTypeCreated;
    private OrderEventType orderEventTypeStarted;
    private OrderEventType orderEventTypeDelivered;
    private MockMvc mvc;

    @BeforeMethod(groups = {"integration-tests"})
    public void setup() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(get("/")
                        .with(user("user").password("password").roles("USER")))
                .apply(springSecurity())
                .build();

        User user = createOrdinaryUser(5);
        Tenant tenant = getTenant(user);

        userRepository.save(user);
        tenantRepository.save(tenant);

        order = getOrder(tenant);
        orderRepository.save(order);

        orderEventTypeCreated = new OrderEventType();
        orderEventTypeCreated.setName(EventTypeEnum.CREATED.toString());

        orderEventTypeStarted = new OrderEventType();
        orderEventTypeStarted.setName(EventTypeEnum.STARTED.toString());

        orderEventTypeDelivered = new OrderEventType();
        orderEventTypeDelivered.setName(EventTypeEnum.DELIVERED.toString());

        orderEventTypeRepository.save(orderEventTypeCreated);
        orderEventTypeRepository.save(orderEventTypeStarted);
        orderEventTypeRepository.save(orderEventTypeDelivered);
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        orderEventRepository.deleteAll();
        orderRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
        orderEventTypeRepository.deleteAll();
    }


    @Test(groups = {"integration-tests"}, expectedExceptions = NestedServletException.class,
            expectedExceptionsMessageRegExp = ".*is not delivering now.*")
    public void testTrackingNotBeginForNotDeliveringOrder() throws Exception {
        //given
        orderEventTypeCreated.setName(EventTypeEnum.CREATED.toString());
        OrderEvent createdEvent = getOrderEvent(order, orderEventTypeCreated, null);
        orderEventRepository.save(createdEvent);

        //when
        mvc.perform(get(String.format("/tracking/orders/%s/", order.getGuid())))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andReturn();
    }

    @Test(groups = {"integration-tests"}, expectedExceptions = NestedServletException.class,
            expectedExceptionsMessageRegExp = ".*is not delivering now.*")
    public void testTrackingNotBeginForDeliveredOrder() throws Exception {
        //given
        orderEventTypeCreated.setName(EventTypeEnum.CREATED.toString());
        OrderEvent createdEvent = getOrderEvent(order, orderEventTypeCreated, null);
        orderEventRepository.save(createdEvent);

        orderEventTypeStarted.setName(EventTypeEnum.STARTED.toString());
        OrderEvent startedEvent = getOrderEvent(order, orderEventTypeStarted, null);
        orderEventRepository.save(startedEvent);

        orderEventTypeDelivered.setName(EventTypeEnum.DELIVERED.toString());
        OrderEvent deliveredEvent = getOrderEvent(order, orderEventTypeDelivered, null);
        orderEventRepository.save(deliveredEvent);

        //when
        mvc.perform(get(String.format("/tracking/orders/%s/", order.getGuid())))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andReturn();
    }

    @Test(groups = {"integration-tests"})
    public void testTrackingBeginForDeliveringOrder() throws Exception {
        //given
        orderEventTypeCreated.setName(EventTypeEnum.CREATED.toString());
        OrderEvent createdEvent = getOrderEvent(order, orderEventTypeCreated, null);
        orderEventRepository.save(createdEvent);

        orderEventTypeStarted.setName(EventTypeEnum.STARTED.toString());
        OrderEvent startedEvent1 = getOrderEvent(order, orderEventTypeStarted, null);
        orderEventRepository.save(startedEvent1);

        OrderEvent startedEvent2 = getOrderEvent(order, orderEventTypeStarted, null);
        orderEventRepository.save(startedEvent2);

        //when
        MvcResult mvcResult = mvc.perform(get(String.format("/tracking/orders/%s/", order.getGuid())))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(content.trim(), "data: {lat: 0, lng: 0}");
    }

    @Test(groups = {"integration-tests"})
    public void testTrackingNotBeginForNotExistingOrder() throws Exception {
        //given
        UUID randomUuid = UUID.randomUUID();
        Order byGuid = orderRepository.findByGuid(randomUuid);
        if (byGuid == null) {
            //when
            mvc.perform(get(String.format("/tracking/orders/%s/", byGuid)))
                    .andExpect(status().isBadRequest());
        }
    }

}
