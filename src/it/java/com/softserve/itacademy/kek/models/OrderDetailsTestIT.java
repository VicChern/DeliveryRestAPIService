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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;

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

    @BeforeClass
    public void setUp() {
        orderDetails = ITTestUtils.getOrderDetails(getOrderForOrderDetails());
        orderDetailsRepository.save(orderDetails);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void whenPayloadSizeMoreThan4096() {
        orderDetails.setPayload(RandomString.make(MAX_PAYLOAD_LENGTH + 1));

        orderDetailsRepository.save(orderDetails);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void whenImageUrlSizeMoreThan512() {
        orderDetails.setImageUrl(RandomString.make(MAX_IMAGE_URL_LENGTH + 1));

        orderDetailsRepository.save(orderDetails);
    }

    private Order getOrderForOrderDetails() {
        User user = ITTestUtils.getUser();
        Tenant tenant = ITTestUtils.getTenant(user);

        userRepository.save(user);
        tenantRepository.save(tenant);

        Order order = ITTestUtils.getOrder(tenant);

        orderRepository.save(order);

        return order;
    }

    @AfterClass
    public void cleanUp() {
        orderDetailsRepository.deleteAll();
        orderRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }
}