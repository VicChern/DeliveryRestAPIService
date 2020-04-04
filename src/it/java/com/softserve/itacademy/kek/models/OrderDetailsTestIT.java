package com.softserve.itacademy.kek.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.TransactionSystemException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderDetails;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_4096;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_512;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrder;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrderDetails;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getTenant;
import static org.testng.Assert.assertEquals;


@Rollback
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderDetailsTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private OrderRepository orderRepository;

    private OrderDetails orderDetails;

    private Order order;

    @BeforeMethod(groups = {"integration-tests"})
    private void setUp() {
        orderDetails = getOrderDetails(getOrderForOrderDetails());
    }

    @AfterMethod(groups = {"integration-tests"})
    private void tearDown() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Rollback
    @Test(groups = {"integration-tests"})
    public void testOrderIsSavedWithValidFields() {
        order.setOrderDetails(orderDetails);

        orderRepository.save(order);

        assertEquals(1, orderRepository.findAll().spliterator().estimateSize());
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = TransactionSystemException.class)
    public void testOrderIsNotSavedWithPayloadMoreThanMaxLength() {
        orderDetails.setPayload(createRandomLetterString(MAX_LENGTH_4096 + 1));
        order.setOrderDetails(orderDetails);

        orderRepository.save(order);

        assertEquals(0, orderRepository.findAll().spliterator().estimateSize());
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = TransactionSystemException.class)
    public void testOrderIsNotSavedWithImageUrlMoreThanMaxLength() {
        orderDetails.setImageUrl(createRandomLetterString(MAX_LENGTH_512 + 1));
        order.setOrderDetails(orderDetails);

        orderRepository.save(order);

        assertEquals(0, orderRepository.findAll().spliterator().estimateSize());
    }

    private Order getOrderForOrderDetails() {
        User user = createOrdinaryUser(1);
        Tenant tenant = getTenant(user);

        userRepository.save(user);
        tenantRepository.save(tenant);

        order = getOrder(tenant);

        return order;
    }
}