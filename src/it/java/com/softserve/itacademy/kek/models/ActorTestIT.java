package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class ActorTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private Actor actor;

    @Test
    public void whenActorIsOk() {

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
    public void whenAliasSizeMoreThan256() {

    }

    @Test
    public void whenAliasSizeLessThan1() {

    }

    @Test
    public void whenAliasIsNull() {

    }

    @Test
    public void whenAliasSizeIsOk() {

    }
}
