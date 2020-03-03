package com.softserve.itacademy.kek.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.enums.ActorRoleEnum;
import com.softserve.itacademy.kek.models.enums.EventType;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IOrderEventService;
import com.softserve.itacademy.kek.services.IOrderService;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrder;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrderEvent;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderEventServiceTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ActorRepository actorRepository;


    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderEventService orderEventService;
    @Autowired
    private OrderEventTypeRepository orderEventTypeRepository;
    @Autowired
    private OrderEventRepository orderEventRepository;
    @Autowired
    private ActorRoleRepository actorRoleRepository;


    private ActorRole actorRole1;
    private ActorRole actorRole2;

    private OrderEventType orderEventTypeCreated;
    private OrderEventType orderEventTypeAssigned;
    private OrderEventType orderEventTypeStarted;
    private OrderEventType orderEventTypeDelivered;

    private User tenantOwner;
    private Tenant tenant;

    private Order order1;
    private Order order2;

    private User savedCustomer1;
    private User savedCustomer2;

    private Order savedOrder1;
    private Order savedOrder2;

    private Actor actor1;
    private Actor actor2;

    private OrderEvent orderEventAssigned;
    private OrderEvent orderEventStarted;
    private OrderEvent orderEventDelivered;

    @BeforeClass(groups = {"integration-tests"})
    public void setUpClass() {

        actorRoleRepository.deleteAll();
        orderEventTypeRepository.deleteAll();

        actorRole1 = new ActorRole();
        actorRole1.setName(ActorRoleEnum.CUSTOMER.toString());
        actorRole2 = new ActorRole();
        actorRole2.setName(ActorRoleEnum.CURRIER.toString());

        orderEventTypeCreated = new OrderEventType();
        orderEventTypeCreated.setName(EventType.CREATED.toString());

        orderEventTypeAssigned = new OrderEventType();
        orderEventTypeAssigned.setName(EventType.ASSIGNED.toString());

        orderEventTypeStarted = new OrderEventType();
        orderEventTypeStarted.setName(EventType.STARTED.toString());

        orderEventTypeDelivered = new OrderEventType();
        orderEventTypeDelivered.setName(EventType.DELIVERED.toString());

        actorRoleRepository.save(actorRole1);
        actorRoleRepository.save(actorRole2);

        orderEventTypeRepository.save(orderEventTypeCreated);
        orderEventTypeRepository.save(orderEventTypeAssigned);
        orderEventTypeRepository.save(orderEventTypeStarted);
        orderEventTypeRepository.save(orderEventTypeDelivered);

    }

    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() {

        tenantOwner = userRepository.save(createOrdinaryUser(1));
        assertNotNull(tenantOwner);

        savedCustomer1 = userRepository.save(createOrdinaryUser(2));
        assertNotNull(tenantOwner);

        tenant = createOrdinaryTenant(1);
        tenant.setTenantOwner(tenantOwner);
        Tenant savedTenant = tenantRepository.save(tenant);
        assertNotNull(savedTenant);

        order1 = getOrder(savedTenant);
        savedOrder1 = (Order) orderService.create(order1, savedCustomer1.getGuid());
        assertNotNull(savedOrder1);

        actor1 = actorRepository.findByUserGuid(savedCustomer1.getGuid()).get();
        orderEventAssigned = getOrderEvent(orderRepository.findByGuid(savedOrder1.getGuid()), orderEventTypeAssigned, actor1);
        orderEventStarted = getOrderEvent(orderRepository.findByGuid(savedOrder1.getGuid()), orderEventTypeStarted, actor1);
        orderEventDelivered = getOrderEvent(orderRepository.findByGuid(savedOrder1.getGuid()), orderEventTypeDelivered, actor1);
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        orderEventRepository.deleteAll();
        actorRepository.deleteAll();
        orderRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterClass(groups = {"integration-tests"})
    public void afterClass() {
        actorRoleRepository.deleteAll();
        orderEventTypeRepository.deleteAll();
    }

    @Test(groups = {"integration-tests"})
    @Transactional
    public void createOrderEventSuccess() {
        //when
        // orderEvent1 with OrderEventType CREATED is created when order is saved, so we don`t need to save it
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventDelivered);

        //then
        assertEquals(orderEventRepository.findAll().size(), 7);
        List<OrderEvent> orderEventList = orderEventRepository.findAll();

        List<EventType> orderEventTypeList = orderEventList
                .stream()
                .map(orderEvent -> EventType.valueOf(orderEvent.getOrderEventType().getName()))
                .distinct()
                .collect(Collectors.toList());

        List<EventType> orderEventTypeList1 = Arrays.asList(EventType.values());
        assertTrue(orderEventTypeList1.containsAll(orderEventTypeList));
    }


    @Test(groups = {"integration-tests"})
    public void getLastAddedEventSuccess() {
        //given
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        IOrderEvent lastSavedEvent = orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventDelivered);

        //when
        OrderEvent lastAddedEvent = (OrderEvent) orderEventService.getLastAddedEvent(savedOrder1.getGuid());

        //then
        assertEquals(lastAddedEvent, lastSavedEvent);
    }

    @Test(groups = {"integration-tests"})
    public void getAllEventsForOrder() {
        //given
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventDelivered);

        //when
        List<IOrderEvent> orderEventList = orderEventService.getAllEventsForOrder(savedOrder1.getGuid());

        //then
        assertEquals(7, orderEventList.size());

        assertTrue(orderEventList
                .stream()
                .allMatch(orderEvent ->
                        orderEvent
                                .getOrder()
                                .getGuid()
                                .equals(savedOrder1.getGuid())));
    }

    @Test(groups = {"integration-tests"})
    public void ifOrderEventCanBeTrackedSuccess() {
        //given
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventDelivered);

        //when
        boolean canBeTracked = orderEventService.ifOrderEventCanBeTracked(savedOrder1.getGuid());

        assertFalse(canBeTracked);
    }

    @Test(groups = {"integration-tests"})
    public void ifOrderEventCanBeTrackedNotSuccess() {
        //given
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);

        //when
        boolean canBeTracked = orderEventService.ifOrderEventCanBeTracked(savedOrder1.getGuid());

        //then
        assertTrue(canBeTracked);
    }

    @Test(groups = {"integration-tests"})
    @Transactional
    public void findAllThatDeliveringNow() {
        //given
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventStarted);
//        orderEventService.createOrderEvent(savedOrder1.getGuid(), actor1.getGuid(), orderEventDelivered);

        order2 = getOrder(tenant);
        savedCustomer2 = userRepository.save(createOrdinaryUser(3));
        savedOrder2 = (Order) orderService.create(order2, savedCustomer2.getGuid());

        actor2 = actorRepository.findByUserGuid(savedCustomer2.getGuid()).get();

        orderEventAssigned = getOrderEvent(orderRepository.findByGuid(savedOrder2.getGuid()), orderEventTypeAssigned, actor2);
        orderEventStarted = getOrderEvent(orderRepository.findByGuid(savedOrder2.getGuid()), orderEventTypeStarted, actor2);

        orderEventService.createOrderEvent(savedOrder2.getGuid(), actor2.getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(savedOrder2.getGuid(), actor2.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder2.getGuid(), actor2.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder2.getGuid(), actor2.getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(savedOrder2.getGuid(), actor2.getGuid(), orderEventStarted);

        //when
        List<IOrderEvent> orderEvents = orderEventService.findAllThatDeliveringNow();

        assertEquals(orderEvents.size(), 2);
        assertTrue(
                orderEvents
                        .stream()
                        .allMatch(orderEvent -> orderEvent.getOrderEventType().getName().equals(EventType.STARTED.toString()))
        );
    }
}
