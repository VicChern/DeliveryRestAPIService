package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_1024;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getActor;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrder;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrderEvent;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrderEventType;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getTenant;

@Rollback
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderEventTestIT extends AbstractTestNGSpringContextTests {

    public static final int MAX_PAYLOAD_LENGTH = MAX_LENGTH_1024;

    @Autowired
    private OrderEventRepository orderEventRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private OrderEventTypeRepository orderEventTypeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;

    private OrderEvent orderEvent1;
    private OrderEvent orderEvent2;

    @BeforeMethod
    public void setUp() {
        orderEvent1 = getOrderEvent(getOrderForOrderEvent(1), getOrderEventTypeForOrderEvent(), null);
        orderEvent2 = getOrderEvent(getOrderForOrderEvent(2), getOrderEventTypeForOrderEvent(), null);
    }

    @AfterMethod
    public void tearDown() {
        orderEventRepository.deleteAll();
        actorRepository.deleteAll();
        orderRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Rollback
    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void testOrderEventIsSavedWithUniqueGuid() {
        orderEventRepository.save(orderEvent1);

        UUID guid = orderEvent1.getGuid();
        orderEvent2.setGuid(guid);

        orderEventRepository.save(orderEvent2);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testOrderEventIsNotSavedWithNullGuid() {
        orderEvent1 = orderEvent2;
        orderEvent1.setGuid(null);

        orderEventRepository.save(orderEvent1);
    }


    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testOrderEventIsNotSavedWithPayloadMoreThanMaxLength() {
        orderEvent1.setPayload(createRandomLetterString(MAX_PAYLOAD_LENGTH + 1));

        orderEventRepository.save(orderEvent1);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testOrderEventIsNotSavedWithEmptyPayload() {
        orderEvent1.setPayload("");

        orderEventRepository.save(orderEvent1);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testUserIsNotSavedWithNullPayload() {
        orderEvent1.setPayload(null);

        orderEventRepository.save(orderEvent1);
    }

    private Order getOrderForOrderEvent(int i) {
        User user = createOrdinaryUser(i);
        Tenant tenant = getTenant(user);

        userRepository.save(user);
        tenantRepository.save(tenant);

        Actor actor = getActor(user, tenant);
        Order order = getOrder(tenant);

        orderRepository.save(order);
        actorRepository.save(actor);

        return order;
    }

    private OrderEventType getOrderEventTypeForOrderEvent() {
        OrderEventType orderEventType = getOrderEventType();

        orderEventTypeRepository.save(orderEventType);

        return orderEventType;
    }
}