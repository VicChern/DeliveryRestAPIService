package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
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

@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderEventTypeTestIT extends AbstractTestNGSpringContextTests {

    public static final int MAX_NAME_LENGTH = 256;

    @Autowired
    private OrderEventTypeRepository orderEventTypeRepository;

    private OrderEventType orderEventType1;
    private OrderEventType orderEventType2;

    @BeforeClass
    public void setUp() {
        orderEventType1 = ITTestUtils.getOrderEventType();
        orderEventType2 = ITTestUtils.getOrderEventType();
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void whenNameSizeMoreThan256() {
        orderEventType1 = orderEventType2;
        orderEventType1.setName(RandomString.make(MAX_NAME_LENGTH + 1));

        orderEventTypeRepository.save(orderEventType1);
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void whenNameSizeLessThan1() {
        orderEventType1 = orderEventType2;
        orderEventType1.setName("");

        orderEventTypeRepository.save(orderEventType1);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenNameIsNull() {
        orderEventType1.setName(null);

        orderEventTypeRepository.save(orderEventType1);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenNameIsNotUnique() {
        orderEventTypeRepository.save(orderEventType1);

        String name = orderEventType1.getName();
        orderEventType2.setName(name);

        orderEventTypeRepository.save(orderEventType2);
    }

    @AfterClass
    public void clearUp() {
        orderEventTypeRepository.deleteAll();
    }
}