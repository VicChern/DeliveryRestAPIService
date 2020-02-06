package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
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
public class OrderTestIT extends AbstractTestNGSpringContextTests {

    public static final int MAX_SUMMARY_LENGTH = 256;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private OrderRepository orderRepository;

    private Order order1;
    private Order order2;

    @Autowired
    private OrderRepository repository;

    @BeforeClass
    public void setUp() {
        order1 = ITTestUtils.getOrder(getTenantForOrder());
        order2 = ITTestUtils.getOrder(getTenantForOrder());
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenGUIDIsNotUnique() {
        orderRepository.save(order1);

        UUID guid = order1.getGuid();
        order2.setGuid(guid);

        orderRepository.save(order2);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenGUIDIsNull() {
        order1.setGuid(null);

        orderRepository.save(order1);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void whenSummarySizeMoreThan256() {
        order1 = order2;
        order1.setSummary(RandomString.make(MAX_SUMMARY_LENGTH + 1));

        orderRepository.save(order1);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void whenSummarySizeLessThan1ThanThrowsException() {
        order1 = order2;
        order1.setSummary("");

        orderRepository.save(order1);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenSummaryIsNull() {
        order1.setSummary(null);

        orderRepository.save(order1);
    }

    private Tenant getTenantForOrder() {
        User user = ITTestUtils.getUser();
        Tenant tenant = ITTestUtils.getTenant(user);

        userRepository.save(user);
        tenantRepository.save(tenant);

        return tenant;
    }

    @AfterClass
    public void clearUp() {
        orderRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }
}