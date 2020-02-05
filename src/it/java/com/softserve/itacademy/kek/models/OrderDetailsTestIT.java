package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderDetailsTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private OrderDetails orderDetails;

    @Test
    public void whenOrderDetailsIsOk() {

    }

    @Test
    public void whenPayloadSizeMoreThan4096() {

    }

    @Test
    public void  whenPayloadSizeIsOk() {

    }

    @Test
    public void whenImageUrlSizeMoreThan512() {

    }

    @Test
    public void whenImageUrlIsOk() {

    }
}
