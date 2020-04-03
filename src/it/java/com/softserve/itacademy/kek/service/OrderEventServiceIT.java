package com.softserve.itacademy.kek.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.enums.ActorRoleEnum;
import com.softserve.itacademy.kek.models.enums.EventTypeEnum;
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
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getActor;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrder;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrderEvent;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderEventServiceIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private OrderEventTypeRepository orderEventTypeRepository;
    @Autowired
    private OrderEventRepository orderEventRepository;
    @Autowired
    private ActorRoleRepository actorRoleRepository;

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderEventService orderEventService;

    private ActorRole customerRole;
    private ActorRole currierRole;

    private OrderEventType orderEventTypeCreated;
    private OrderEventType orderEventTypeAssigned;
    private OrderEventType orderEventTypeStarted;
    private OrderEventType orderEventTypeDelivered;

    private User user;
    private User customer;
    private Tenant tenant;
    private Order order;
    private Actor actor;

    private OrderEvent orderEventAssigned;
    private OrderEvent orderEventStarted;
    private OrderEvent orderEventDelivered;

    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() {
        customerRole = new ActorRole();
        customerRole.setName(ActorRoleEnum.CUSTOMER.name());
        currierRole = new ActorRole();
        currierRole.setName(ActorRoleEnum.CURRIER.name());

        actorRoleRepository.save(customerRole);
        actorRoleRepository.save(currierRole);

        orderEventTypeCreated = new OrderEventType();
        orderEventTypeCreated.setName(EventTypeEnum.CREATED.toString());

        orderEventTypeAssigned = new OrderEventType();
        orderEventTypeAssigned.setName(EventTypeEnum.ASSIGNED.toString());

        orderEventTypeStarted = new OrderEventType();
        orderEventTypeStarted.setName(EventTypeEnum.STARTED.toString());

        orderEventTypeDelivered = new OrderEventType();
        orderEventTypeDelivered.setName(EventTypeEnum.DELIVERED.toString());

        orderEventTypeRepository.save(orderEventTypeCreated);
        orderEventTypeRepository.save(orderEventTypeAssigned);
        orderEventTypeRepository.save(orderEventTypeStarted);
        orderEventTypeRepository.save(orderEventTypeDelivered);

        user = createOrdinaryUser(1);
        customer = createOrdinaryUser(2);
        tenant = createOrdinaryTenant(1);

        final User savedUser = userRepository.save(user);
        assertNotNull(savedUser);

        final User savedCustomer = userRepository.save(customer);
        assertNotNull(savedCustomer);

        tenant.setTenantOwner(savedUser);
        final Tenant savedTenant = tenantRepository.save(tenant);
        assertNotNull(savedTenant);

        order = (Order) orderService.create(getOrder(tenant), customer.getGuid());
        assertNotNull(order);

        actor = actorRepository.findByUserGuid(customer.getGuid()).get();
        orderEventAssigned = getOrderEvent(order, orderEventTypeAssigned, actor);
        orderEventStarted = getOrderEvent(order, orderEventTypeStarted, actor);
        orderEventDelivered = getOrderEvent(order, orderEventTypeDelivered, actor);
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
        //when
        final Actor currier = getActor(user, tenant, currierRole);

        final OrderEvent orderEvent = getOrderEvent(orderRepository.findByGuid(order.getGuid()), orderEventTypeAssigned, currier);

        final IOrderEvent createdOrderEvent = orderEventService.create(orderEvent, order.getGuid());

        //than
        final IOrderEvent foundOrderEvent = orderEventRepository.findByGuid(createdOrderEvent.getGuid()).orElse(null);

        assertEquals(createdOrderEvent.getPayload(), foundOrderEvent.getPayload());
    }

    @Rollback
    @Test(groups = {"integration-tests"})
    public void getAllEventsForOrderSuccess() {
        //when
        final Actor currier = getActor(user, tenant, currierRole);
        final Actor createdCurrier = actorRepository.save(currier);

        final OrderEvent orderEvent1 = getOrderEvent(order, orderEventTypeAssigned, createdCurrier);
        final OrderEvent orderEvent2 = getOrderEvent(order, orderEventTypeStarted, createdCurrier);
        final OrderEvent orderEvent3 = getOrderEvent(order, orderEventTypeDelivered, createdCurrier);

        orderEventRepository.save(orderEvent1);
        orderEventRepository.save(orderEvent2);
        orderEventRepository.save(orderEvent3);

        final List<IOrderEvent> orderEventList = orderEventService.getAllEventsForOrder(order.getGuid());

        //then
        assertEquals(orderEventList.size(), 4);
        assertEquals(orderEventList.get(1).getGuid(), orderEvent1.getGuid());
        assertEquals(orderEventList.get(2).getGuid(), orderEvent2.getGuid());
        assertEquals(orderEventList.get(3).getGuid(), orderEvent3.getGuid());
    }

    @Test(groups = {"integration-tests"})
    @Transactional
    public void createOrderEventSuccess() {
        //when
        // orderEvent1 with OrderEventType CREATED is created when order is saved, so we don`t need to save it
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventDelivered);

        //then
        assertEquals(orderEventRepository.findAll().size(), 7);
        final List<OrderEvent> orderEventList = orderEventRepository.findAll();

        final List<EventTypeEnum> orderEventTypeEnumList = orderEventList
                .stream()
                .map(orderEvent -> EventTypeEnum.valueOf(orderEvent.getOrderEventType().getName()))
                .distinct()
                .collect(Collectors.toList());

        final List<EventTypeEnum> orderEventTypeEnumList1 = Arrays.asList(EventTypeEnum.values());
        assertTrue(orderEventTypeEnumList1.containsAll(orderEventTypeEnumList));
    }

    @Test(groups = {"integration-tests"})
    public void getLastAddedEventSuccess() {
        //given
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);

        final IOrderEvent lastSavedEvent = orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventDelivered);

        //when
        final OrderEvent lastAddedEvent = (OrderEvent) orderEventService.getLastAddedEvent(order.getGuid());

        //then
        assertEquals(lastAddedEvent, lastSavedEvent);
    }

    @Test(groups = {"integration-tests"})
    public void getAllEventsForOrder() {
        //given
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventDelivered);

        //when
        final List<IOrderEvent> orderEventList = orderEventService.getAllEventsForOrder(order.getGuid());

        //then
        assertEquals(7, orderEventList.size());

        assertTrue(orderEventList
                .stream()
                .allMatch(orderEvent ->
                        orderEvent
                                .getOrder()
                                .getGuid()
                                .equals(order.getGuid())));
    }

    @Test(groups = {"integration-tests"})
    public void ifOrderEventCanBeTrackedSuccess() {
        //given
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);

        //when
        final boolean canBeTracked = orderEventService.ifOrderEventCanBeTracked(order.getGuid());

        assertTrue(canBeTracked);
    }

    @Test(groups = {"integration-tests"})
    public void ifOrderEventCanBeTrackedNotSuccess() {
        //given
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventDelivered);

        //when
        final boolean canBeTracked = orderEventService.ifOrderEventCanBeTracked(order.getGuid());

        //then
        assertFalse(canBeTracked);
    }

    @Test(groups = {"integration-tests"})
    @Transactional
    public void findAllThatDeliveringNow() {
        //given
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order.getGuid(), actor.getUser().getGuid(), orderEventStarted);

        final User customer2 = userRepository.save(createOrdinaryUser(3));
        final Order order2 = (Order) orderService.create(getOrder(tenant), customer2.getGuid());
        final Actor actor2 = actorRepository.findByUserGuid(customer2.getGuid()).get();

        orderEventAssigned = getOrderEvent(orderRepository.findByGuid(order2.getGuid()), orderEventTypeAssigned, actor2);
        orderEventStarted = getOrderEvent(orderRepository.findByGuid(order2.getGuid()), orderEventTypeStarted, actor2);

        orderEventService.createOrderEvent(order2.getGuid(), actor2.getUser().getGuid(), orderEventAssigned);
        orderEventService.createOrderEvent(order2.getGuid(), actor2.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order2.getGuid(), actor2.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order2.getGuid(), actor2.getUser().getGuid(), orderEventStarted);
        orderEventService.createOrderEvent(order2.getGuid(), actor2.getUser().getGuid(), orderEventStarted);

        //when
        final List<IOrderEvent> orderEvents = orderEventService.findAllThatDeliveringNow();

        assertEquals(orderEvents.size(), 2);
        assertTrue(
                orderEvents
                        .stream()
                        .allMatch(orderEvent -> orderEvent.getOrderEventType().getName().equals(EventTypeEnum.STARTED.toString()))
        );
    }
}
