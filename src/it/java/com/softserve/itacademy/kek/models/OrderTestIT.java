package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.validation.Valid;

@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderTestIT extends AbstractTestNGSpringContextTests {

    @Valid
    private Order order = new Order();

    @Autowired
    OrderTestIT() {
        order.setSummary("asd");
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
    public void whenSummarySizeLessThan1() {
        order.setSummary("");

        Assert.assertEquals(order.getSummary(), "asd");
    }

    @Test
    public void whenSummaryIsNull() {

    }

    @Test
    public void whenSummarySizeIsOk() {

    }
}
