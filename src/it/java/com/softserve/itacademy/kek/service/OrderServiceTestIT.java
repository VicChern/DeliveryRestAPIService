package com.softserve.itacademy.kek.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderDetails;
import com.softserve.itacademy.kek.models.enums.ActorRoleEnum;
import com.softserve.itacademy.kek.models.enums.EventType;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderDetails;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.repositories.OrderDetailsRepository;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IOrderService;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrder;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderServiceTestIT extends AbstractTestNGSpringContextTests {

    public static final String newSummary = "new summary";
    public static final String newImageUrl = "new image url";


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private OrderEventTypeRepository orderEventTypeRepository;
    @Autowired
    private OrderEventRepository orderEventRepository;
    @Autowired
    private ActorRoleRepository actorRoleRepository;

    @Autowired
    private IOrderService orderService;

    private ActorRole actorRole1;
    private ActorRole actorRole2;

    private OrderEventType orderEventType1;
    private OrderEventType orderEventType2;
    private OrderEventType orderEventType3;
    private OrderEventType orderEventType4;

    private User user;
    private User customer;
    private Tenant tenant;
    private Order order;

    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() {
        actorRole1 = new ActorRole();
        actorRole1.setName(ActorRoleEnum.CUSTOMER.toString());
        actorRole2 = new ActorRole();
        actorRole2.setName(ActorRoleEnum.CURRIER.toString());

        orderEventType1 = new OrderEventType();
        orderEventType1.setName(EventType.CREATED.toString());

        orderEventType2 = new OrderEventType();
        orderEventType2.setName(EventType.ASSIGNED.toString());

        orderEventType3 = new OrderEventType();
        orderEventType3.setName(EventType.STARTED.toString());

        orderEventType4 = new OrderEventType();
        orderEventType4.setName(EventType.DELIVERED.toString());

        actorRoleRepository.save(actorRole1);
        actorRoleRepository.save(actorRole2);

        orderEventTypeRepository.save(orderEventType1);
        orderEventTypeRepository.save(orderEventType2);
        orderEventTypeRepository.save(orderEventType3);
        orderEventTypeRepository.save(orderEventType4);

        user = createOrdinaryUser(1);
        customer = createOrdinaryUser(2);
        tenant = createOrdinaryTenant(1);

        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);

        User savedCustomer = userRepository.save(customer);
        assertNotNull(savedCustomer);

        tenant.setTenantOwner(savedUser);
        Tenant savedTenant = tenantRepository.save(tenant);
        assertNotNull(savedTenant);

        order = getOrder(tenant);
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        orderEventRepository.deleteAll();
        actorRepository.deleteAll();
        orderRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
        actorRoleRepository.deleteAll();
        orderEventTypeRepository.deleteAll();
    }

    @Rollback
    @Test(groups = {"integration-tests"})
    public void createSuccess() {
        //given
        IOrder createdOrder = orderService.create(order, customer.getGuid());

        //when
        IOrder foundOrder = orderRepository.findByGuid(createdOrder.getGuid());

        IOrderDetails foundDetails = orderDetailsRepository.findByOrder(createdOrder);

        //then
        assertEquals(createdOrder.getGuid(), foundOrder.getGuid());
        assertEquals(createdOrder.getOrderDetails(), foundDetails);
        assertEquals(createdOrder.getSummary(), foundOrder.getSummary());
        assertEquals(createdOrder.getTenant(), foundOrder.getTenant());
    }

    @Rollback
    @Test(groups = {"integration-tests"})
    public void updateSuccess() {
        //given
        Order createdOrder = orderRepository.save(order);

        OrderDetails orderDetails = orderDetailsRepository.findByOrder(order);
        orderDetails.setImageUrl(newImageUrl);

        createdOrder.setSummary(newSummary);
        createdOrder.setOrderDetails(orderDetails);

        //when
        IOrder updatedOrder = orderService.update(order, order.getGuid());

        IOrder foundOrder = orderRepository.findByGuid(order.getGuid());

        IOrderDetails foundDetails = orderDetailsRepository.findByOrder(createdOrder);

        //then
        assertNotNull(createdOrder);
        assertNotNull(updatedOrder);
        assertEquals(updatedOrder.getSummary(), newSummary);

        assertEquals(updatedOrder.getGuid(), foundOrder.getGuid());
        assertEquals(updatedOrder.getOrderDetails(), foundDetails);
        assertEquals(updatedOrder.getSummary(), foundOrder.getSummary());
        assertEquals(updatedOrder.getTenant(), foundOrder.getTenant());
    }

    @Rollback
    @Test(groups = {"integration-tests"})
    public void getByGuidSuccess() {
        //given
        Order createdOrder = orderRepository.save(order);

        //when
        IOrder foundOrder = orderService.getByGuid(order.getGuid());

        //then
        IOrderDetails foundDetails = orderDetailsRepository.findByOrder(createdOrder);

        assertNotNull(createdOrder);
        assertNotNull(foundOrder);
        assertEquals(createdOrder.getGuid(), foundOrder.getGuid());
        assertEquals(createdOrder.getOrderDetails(), foundDetails);
        assertEquals(createdOrder.getSummary(), foundOrder.getSummary());
        assertEquals(createdOrder.getTenant(), foundOrder.getTenant());
    }

    @Rollback
    @Test(groups = {"integration-tests"})
    public void getAllByTenantGuidSuccess() {
        //given
        Order createdOrder = orderRepository.save(order);

        //when
        List<IOrder> orderList = orderService.getAllByTenantGuid(tenant.getGuid());

        //then
        assertNotNull(createdOrder);
        assertNotNull(orderList);
        assertEquals(orderList.size(), 1);
    }

    @Rollback
    @Test(groups = {"integration-tests"})
    public void getAllSuccess() {
        //given
        Order createdOrder = orderRepository.save(order);

        //when
        List<IOrder> orderList = orderService.getAll();

        //then
        assertNotNull(createdOrder);
        assertNotNull(orderList);
        assertEquals(orderList.size(), 1);
    }

    @Rollback
    @Test(groups = {"integration-tests"})
    public void deleteByGuidSuccess() {
        //given
        IOrder createdOrder = orderService.create(order, customer.getGuid());

        //when
        orderService.deleteByGuid(createdOrder.getGuid());

        //then
        Order foundOrder = orderRepository.findByGuid(createdOrder.getGuid());
        OrderDetails foundOrderDetails = orderDetailsRepository.findByOrder(createdOrder);

        assertNotNull(createdOrder);
        assertNull(foundOrder);
        assertNull(foundOrderDetails);
    }
}
