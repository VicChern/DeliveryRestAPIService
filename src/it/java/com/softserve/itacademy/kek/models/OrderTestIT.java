package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
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

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_256;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrder;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getUser;

@Rollback
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderTestIT extends AbstractTestNGSpringContextTests {

    private static final int MAX_SUMMARY_LENGTH = MAX_LENGTH_256;

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

    @BeforeMethod
    public void setUp() {
        order1 = getOrder(getTenantForOrder());
        order2 = getOrder(getTenantForOrder());
    }

    @AfterMethod
    public void tearDown() {
        orderRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Rollback
    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void testOrderIsSavedWithUniqueGuid() {
        orderRepository.save(order1);

        UUID guid = order1.getGuid();
        order2.setGuid(guid);

        orderRepository.save(order2);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testOrderIsNotSavedWithNullGuid() {
        order1.setGuid(null);

        orderRepository.save(order1);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testOrderIsNotSavedWithSummaryMoreThanMaxLength() {
        order1 = order2;
        order1.setSummary(createRandomLetterString(MAX_SUMMARY_LENGTH + 1));

        orderRepository.save(order1);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testOrderIsNotSavedWithEmptySummary() {
        order1 = order2;
        order1.setSummary("");

        orderRepository.save(order1);
    }

    @Rollback
    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testOrderIsNotSavedWithNullSummary() {
        order1.setSummary(null);

        orderRepository.save(order1);
    }

    private Tenant getTenantForOrder() {
        User user = getUser();
        Tenant tenant = getTenant(user);

        userRepository.save(user);
        tenantRepository.save(tenant);

        return tenant;
    }

}