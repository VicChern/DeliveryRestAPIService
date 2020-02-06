package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.utils.ITTestUtils;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderEventTestIT extends AbstractTestNGSpringContextTests {

    public static final int MAX_PAYLOAD_LENGTH = 1024;

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

    @BeforeClass
    public void setUp() {
        orderEvent1 = ITTestUtils.getOrderEvent(getOrderForOrderEvent(), getActorForOrderEvent(), getOrderEventTypeForOrderEvent());
        orderEvent2 = ITTestUtils.getOrderEvent(getOrderForOrderEvent(), getActorForOrderEvent(), getOrderEventTypeForOrderEvent());
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenGUIDIsNotUnique() {
        orderEventRepository.save(orderEvent1);

        UUID guid = orderEvent1.getGuid();
        orderEvent2.setGuid(guid);

        orderEventRepository.save(orderEvent2);
        orderEventRepository.deleteAll();
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenGUIDIsNull() {
        orderEvent1 = orderEvent2;
        orderEvent1.setGuid(null);

        orderEventRepository.save(orderEvent1);
        orderEventRepository.deleteAll();
    }


    @Test(expectedExceptions = ConstraintViolationException.class)
    public void whenPayloadSizeMoreThan1024() {
        orderEvent1.setPayload(RandomString.make(MAX_PAYLOAD_LENGTH + 1));

        orderEventRepository.save(orderEvent1);
        orderEventRepository.deleteAll();
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void whenPayloadSizeLessThan1() {
        orderEvent1.setPayload("");

        orderEventRepository.save(orderEvent1);
        orderEventRepository.deleteAll();
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenPayloadIsNull() {
        orderEvent1.setPayload(null);

        orderEventRepository.save(orderEvent1);
        orderEventRepository.deleteAll();
    }

    private Actor getActorForOrderEvent() {
        User user = ITTestUtils.getUser();
        Tenant tenant = ITTestUtils.getTenant(user);

        userRepository.save(user);
        tenantRepository.save(tenant);

        Actor actor = ITTestUtils.getActor(user, tenant);

        actorRepository.save(actor);

        return actor;
    }

    private Order getOrderForOrderEvent() {
        User user = ITTestUtils.getUser();
        Tenant tenant = ITTestUtils.getTenant(user);

        userRepository.save(user);
        tenantRepository.save(tenant);

        Order order = ITTestUtils.getOrder(tenant);

        orderRepository.save(order);

        return order;
    }

    private OrderEventType getOrderEventTypeForOrderEvent() {
        OrderEventType orderEventType = ITTestUtils.getOrderEventType();

        orderEventTypeRepository.save(orderEventType);

        return orderEventType;
    }

    @AfterClass
    public void cleanUp() {
        orderEventRepository.deleteAll();
        orderEventTypeRepository.deleteAll();
        actorRepository.deleteAll();
        orderRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }
}