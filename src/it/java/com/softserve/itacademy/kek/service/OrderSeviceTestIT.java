package com.softserve.itacademy.kek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IOrderService;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrder;
import static org.testng.Assert.assertNotNull;

@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderSeviceTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderEventTypeRepository orderEventTypeRepository;
    @Autowired
    private OrderEventRepository orderEventRepository;

    private User user;
    private Tenant tenant;
    private Order order;

    @BeforeMethod
    public void setUp() {
        user = createOrdinaryUser(1);
        tenant = createOrdinaryTenant(1);

        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);

        tenant.setTenantOwner(savedUser);
        Tenant savedTenant = tenantRepository.save(tenant);
        assertNotNull(savedTenant);

        order = getOrder(tenant);

    }

    @AfterMethod
    public void tearDown() {
        orderEventRepository.deleteAll();
        orderEventTypeRepository.deleteAll();
        orderRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Rollback
    @Test
    public void createSuccess() {
        //when
        orderService.create(order);
    }

}
