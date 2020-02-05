package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderEventTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private OrderEvent orderEvent;

    @Test
    public void whenOrderEventIsOk() {

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
    public void whenGUIDIsNotNull() {

    }

    @Test
    public void whenPayloadSizeMoreThan1024() {

    }

    @Test
    public void whenPayloadSizeLessThan1() {

    }

    @Test
    public void whenPayloadIsNull() {

    }

    @Test
    public void whenPayloadSizeIsOk() {

    }
}
