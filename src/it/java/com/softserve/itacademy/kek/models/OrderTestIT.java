package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.validation.Valid;
import java.util.UUID;

@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderTestIT extends AbstractTestNGSpringContextTests {

    @Valid
    private Order order = new Order();

    @Autowired
    private OrderRepository repository;

    @Autowired
    OrderTestIT() {
        order.setSummary("summary");
        order.setGuid(UUID.randomUUID());
    }

    @Test
    public void whenGUIDIsNotUnique() {

    }

    @Test
    public void whenGUIDIsUnique() {

    }

    @Test
    public void whenGUIDIsNull() {

    }

    @Test
    public void whenGUIDIsOk() {

    }

    @Test
    public void whenSummarySizeMoreThan256() {

    }

    @Test
    public void whenSummarySizeLessThan1ThanThrowsException() {
        String summary = "";

        order.setSummary(summary);

        Order savedOrder = repository.save(order);

        Assert.assertEquals(savedOrder, order);
    }

    @Test
    public void whenSummaryIsNull() {

    }

    @Test
    public void whenSummarySizeIsOk() {

    }
}
