package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.OrderDetailsRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.utils.ITTestUtils;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.TransactionSystemException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


@Rollback
@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderDetailsTestIT extends AbstractTestNGSpringContextTests {

    public static final int MAX_PAYLOAD_LENGTH = 4096;
    public static final int MAX_IMAGE_URL_LENGTH = 512;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    private OrderDetails orderDetails;

    private Order order;

    @BeforeMethod
    public void setUp() {
        orderDetails = ITTestUtils.getOrderDetails(getOrderForOrderDetails());
    }

    @Rollback
    @Test
    public void whenSaveWithValidData() {
        order.setOrderDetails(orderDetails);

        orderRepository.save(order);

        assertEquals(1, orderRepository.findAll().spliterator().estimateSize());
        assertEquals(1, orderDetailsRepository.findAll().spliterator().estimateSize());
    }

    @Rollback
    @Test(expectedExceptions = TransactionSystemException.class)
    public void whenPayloadSizeMoreThan4096() {
        orderDetails.setPayload(RandomString.make(MAX_PAYLOAD_LENGTH + 1));
        order.setOrderDetails(orderDetails);

        orderRepository.save(order);

        assertEquals(0, orderRepository.findAll().spliterator().estimateSize());
        assertEquals(0, orderDetailsRepository.findAll().spliterator().estimateSize());
    }

    @Rollback
    @Test(expectedExceptions = TransactionSystemException.class)
    public void whenImageUrlSizeMoreThan512() {
        orderDetails.setImageUrl(RandomString.make(MAX_IMAGE_URL_LENGTH + 1));
        order.setOrderDetails(orderDetails);

        orderRepository.save(order);

        assertEquals(0, orderRepository.findAll().spliterator().estimateSize());
        assertEquals(0, orderDetailsRepository.findAll().spliterator().estimateSize());
    }

    private Order getOrderForOrderDetails() {
        User user = ITTestUtils.getUser();
        Tenant tenant = ITTestUtils.getTenant(user);
        order = ITTestUtils.getOrder(tenant);

        userRepository.save(user);
        tenantRepository.save(tenant);

        return order;
    }
}