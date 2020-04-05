package com.softserve.itacademy.kek.models;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.MAX_LENGTH_256;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createRandomLetterString;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrderEventType;

@Rollback
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderEventTypeEnumTestIT extends AbstractTestNGSpringContextTests {

    public static final int MAX_NAME_LENGTH = MAX_LENGTH_256;

    @Autowired
    private OrderEventTypeRepository orderEventTypeRepository;

    private OrderEventType orderEventType1;
    private OrderEventType orderEventType2;

    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() {
        orderEventType1 = getOrderEventType();
        orderEventType2 = getOrderEventType();
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        orderEventTypeRepository.deleteAll();
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = ConstraintViolationException.class)
    public void testOrderEventTypeIsNotSavedWithNameMoreThanMaxLength() {
        orderEventType1 = orderEventType2;
        orderEventType1.setName(createRandomLetterString(MAX_NAME_LENGTH + 1));

        orderEventTypeRepository.save(orderEventType1);
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = ConstraintViolationException.class)
    public void testOrderEventTypeIsNotSavedWithEmptyName() {
        orderEventType1 = orderEventType2;
        orderEventType1.setName("");

        orderEventTypeRepository.save(orderEventType1);
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = ConstraintViolationException.class)
    public void testOrderEventTypeIsNotSavedWithNullName() {
        orderEventType1.setName(null);

        orderEventTypeRepository.save(orderEventType1);
    }

    @Rollback
    @Test(groups = {"integration-tests"}, expectedExceptions = DataIntegrityViolationException.class)
    public void testUserIsSavedWithUniqueName() {
        orderEventTypeRepository.save(orderEventType1);

        String name = orderEventType1.getName();
        orderEventType2.setName(name);

        orderEventTypeRepository.save(orderEventType2);
    }

}